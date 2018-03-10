package io.ipfs.multihash;

import io.ipfs.multibase.Base16;
import io.ipfs.multibase.Base58;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class Multihash {
    public enum Type {
        md5(0xd5, 16, "MD5"),
        sha1(0x11, 20, "SHA-1"),
        sha2_256(0x12, 32, "SHA-256"),
        sha2_512(0x13, 64, "SHA-512"),
        sha3_512(0x14, 64, "N/A"),
        blake2b(0x40, 64, "N/A"),
        blake2s(0x41, 32, "N/A");

        public final int index, length;
        public final String name;

        Type(final int index, final int length, final String name) {
            this.index = index;
            this.length = length;
            this.name = name;
        }

        private static Map<Integer, Type> lookup = new TreeMap<>();
        static {
            for (Type t: Type.values())
                lookup.put(t.index, t);
        }

        public static Type lookup(int t) {
            if (!lookup.containsKey(t))
                throw new IllegalStateException("Unknown Multihash type: "+t);
            return lookup.get(t);
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private final Type type;
    private final byte[] hash;

    public Multihash(final Type type, final byte[] hash) {
        if (hash.length > 127)
            throw new IllegalStateException("Unsupported hash size: "+hash.length);
        if (hash.length != type.length)
            throw new IllegalStateException("Incorrect hash length: " + hash.length + " != "+type.length);
        this.type = type;
        this.hash = hash;
    }

    public Multihash(Multihash toClone) {
        this(toClone.type, toClone.hash); // N.B. despite being a byte[], hash is immutable
    }

    public Multihash(final byte[] multihash) {
        this(Type.lookup(multihash[0] & 0xff), Arrays.copyOfRange(multihash, 2, multihash.length));
    }

    public byte[] toBytes() {
        byte[] res = new byte[hash.length+2];
        res[0] = (byte)type.index;
        res[1] = (byte)hash.length;
        System.arraycopy(hash, 0, res, 2, hash.length);
        return res;
    }

    public Type getType() {
        return type;
    }

    public byte[] getHash() {
        return Arrays.copyOf(hash, hash.length);
    }

    public void serialize(DataOutput dout) throws IOException {
        dout.write(toBytes());
    }

    public static Multihash deserialize(DataInput din) throws IOException {
        int type = din.readUnsignedByte();
        int len = din.readUnsignedByte();
        Type t = Type.lookup(type);
        byte[] hash = new byte[len];
        din.readFully(hash);
        return new Multihash(t, hash);
    }

    public static Multihash hash(final String input, final Type type) throws IOException {

        try {
            MessageDigest md = MessageDigest.getInstance(type.toString());
            md.update(input.getBytes("UTF-8"));

            byte[] digest = md.digest();
            return new Multihash(type, digest);

        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new IOException("Unable to hash input: " + input + " with Type: " + type);
        }

    }

    @Override
    public String toString() {
        return toBase58();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Multihash))
            return false;
        return type == ((Multihash) o).type && Arrays.equals(hash, ((Multihash) o).hash);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hash) ^ type.hashCode();
    }

    public String toHex() {
        return Base16.encode(toBytes());
    }

    public String toBase58() {
        return Base58.encode(toBytes());
    }

    public static Multihash fromHex(String hex) {
        if (hex.length() % 2 != 0)
            throw new IllegalStateException("Uneven number of hex digits!");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        for (int i=0; i < hex.length()-1; i+= 2)
            bout.write(Integer.valueOf(hex.substring(i, i+2), 16));
        return new Multihash(bout.toByteArray());
    }

    public static Multihash fromBase58(String base58) {
        return new Multihash(Base58.decode(base58));
    }
}
