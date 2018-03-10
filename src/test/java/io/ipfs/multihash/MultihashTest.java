package io.ipfs.multihash;

import io.ipfs.multibase.Base58;
import org.junit.Test;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MultihashTest {

    @Test
    public void base58Test() {
        List<String> examples = Arrays.asList("QmPZ9gcCEpqKTo6aq61g2nXGUhM4iCL3ewB6LDXZCtioEB",
                "QmatmE9msSfkKxoffpHwNLNKgwZG8eT9Bud6YoPab52vpy");
        for (String example: examples) {
            byte[] output = Base58.decode(example);
            String encoded = Base58.encode(output);
            if (!examples.contains(encoded))
                throw new IllegalStateException("Incorrect base58! " + example + " => " + encoded);
        }
    }

    @Test
    public void multihashTest() {
        Object[][] examples = new Object[][]{
            {Multihash.Type.md5, "9qZY4e2uauH3bG83FdaPSaPzA", "hello world"},
            {Multihash.Type.sha1, "5drNu81uhrFLRiS4bxWgAkpydaLUPW", "hello world"},
            {Multihash.Type.sha2_256, "QmaozNR7DZHQK1ZcU9p7QdrshMvXqWK6gpu5rmrkPdT3L4", "hello world"},
            {Multihash.Type.sha2_512, "8Vtkv2tdQ43bNGdWN9vNx9GVS9wrbXHk4ZW8kmucPmaYJwwedXir52kti9wJhcik4HehyqgLrQ1hBuirviLhxgRBNv", "hello world"}
            // SHA3 not yet implemented in standard Java library. https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#MessageDigest
            // {Multihash.Type.sha3_512, "8tWhXW5oUwtPd9d3FnjuLP1NozN3vc45rmsoWEEfrZL1L6gi9dqi1YkZu5iHb2HJ8WbZaaKAyNWWRAa8yaxMkGKJmX", "hello world"}
        };

        for(Object[] ex: examples) {
            Multihash m = Multihash.fromBase58((String)ex[1]);
            try {
                MessageDigest md = MessageDigest.getInstance(ex[0].toString());
                assert(md != null);
                md.update(((String) ex[2]).getBytes("UTF-8"));
                byte[] digest = md.digest();
                // Test constructor
                Multihash m2 = new Multihash((Multihash.Type)ex[0], digest);
                // Test comparison
                assert(m2.equals(m));
                // Test conversions
                assert(m.toBase58().equals(m2.toBase58()));
                assert(m.toBase58().equals(ex[1]));
                // Test fromHex and toHex
                Multihash m3 = Multihash.fromHex(m.toHex());
                assert(m3.equals(m));
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                assert(false);
            }
        }
    }

    @Test
    public void multihashAlgorithmsTest() throws IOException {

        multihashGenericAlgorithmTest(Multihash.Type.sha1, "hello world",
                "5drNu81uhrFLRiS4bxWgAkpydaLUPW", "11142aae6c35c94fcfb415dbe95f408b9ce91ee846ed");
        multihashGenericAlgorithmTest(Multihash.Type.sha2_256, "hello world",
                "QmaozNR7DZHQK1ZcU9p7QdrshMvXqWK6gpu5rmrkPdT3L4", "1220b94d27b9934d3e08a52e52d7da7dabfac484efe37a5380ee9088f7ace2efcde9");
    }

    private void multihashGenericAlgorithmTest(Multihash.Type type, String input, String base58, String base16) throws IOException {
        Multihash multihash = Multihash.hash(input, type);
        assertEquals(base58, multihash.toBase58());
        assertEquals(base16, multihash.toHex());
    }
}
