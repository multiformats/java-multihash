package io.ipfs.multihash;

import io.ipfs.multibase.*;

import java.io.*;
import java.util.*;

public class Multihash {
    public enum Type {
        md5(0xd5, 16),
        sha1(0x11, 20),
        sha2_256(0x12, 32),
        sha2_512(0x13, 64),
        sha3_224(0x17, 24),
        sha3_256(0x16, 32),
        sha3_512(0x14, 64),
        keccak_224(0x1a, 24),
        keccak_256(0x1b, 32),
        keccak_384(0x1c, 48),
        keccak_512(0x1d, 64),
        blake2b(0x40, 64),
        blake2s(0x41, 32);

        public int index, length;

        Type(int index, int length) {
            this.index = index;
            this.length = length;
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
    }

    public final Type type;
    private final byte[] hash;

    public Multihash(Type type, byte[] hash) {
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

    public Multihash(byte[] multihash) {
        this(Type.lookup(multihash[0] & 0xff), Arrays.copyOfRange(multihash, 2, multihash.length));
    }

    public byte[] toBytes() {
        byte[] res = new byte[hash.length+2];
        res[0] = (byte)type.index;
        res[1] = (byte)hash.length;
        System.arraycopy(hash, 0, res, 2, hash.length);
        return res;
    }

    public byte[] getHash() {
        return hash;
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

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public String toHex() {
        byte[] bytes = toBytes();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
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
