package io.ipfs.multihash;

import io.ipfs.multibase.Base16;
import io.ipfs.multibase.Base58;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class Multihash {
    public enum Type {
        id(0, -1),
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

        public final int index, length;

        Type(final int index, final int length) {
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

    private final Type type;
    private final byte[] hash;

    public Multihash(final Type type, final byte[] hash) {
        if (hash.length > 127)
            throw new IllegalStateException("Unsupported hash size: "+hash.length);
        if (hash.length != type.length && type != Type.id)
            throw new IllegalStateException("Incorrect hash length: " + hash.length + " != "+type.length);
        if (type == Type.id && hash.length > 64)
            throw new IllegalStateException("Unsupported size for identity hash! "+ hash.length);
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
            throw new IllegalStateException("Odd number of hex digits!");

        try (ByteArrayOutputStream bout = new ByteArrayOutputStream()) {
            for (int i = 0; i < hex.length() - 1; i += 2)
                bout.write(Integer.valueOf(hex.substring(i, i + 2), 16));
            return new Multihash(bout.toByteArray());
        } catch (IOException e) {
            throw new IllegalStateException("Unable to handle Multihash conversion to Hex properly");
        }
    }

    public static Multihash fromBase58(String base58) {
        return new Multihash(Base58.decode(base58));
    }
}
