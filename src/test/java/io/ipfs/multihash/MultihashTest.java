package io.ipfs.multihash;

import org.junit.*;
import io.ipfs.multibase.*;
import java.util.*;
import java.security.MessageDigest;

public class MultihashTest {

    @Test
    public void base58Test() {
        List<String> examples = Arrays.asList("QmPZ9gcCEpqKTo6aq61g2nXGUhM4iCL3ewB6LDXZCtioEB",
                "QmatmE9msSfkKxoffpHwNLNKgwZG8eT9Bud6YoPab52vpy");
        for (String example: examples) {
            byte[] output = Base58.decode(example);
            String encoded = Base58.encode(output);
            if (!encoded.equals(example))
                throw new IllegalStateException("Incorrect base58! " + example + " => " + encoded);
        }
    }

    @Test
    public void multihashTest() {
        Object[][] examples = new Object[][]{
            {Multihash.Type.sha1, "SHA-1", "5drNu81uhrFLRiS4bxWgAkpydaLUPW", "hello world"},
            {Multihash.Type.sha2_256, "SHA-256", "QmaozNR7DZHQK1ZcU9p7QdrshMvXqWK6gpu5rmrkPdT3L4", "hello world"},
            {Multihash.Type.sha2_512, "SHA-512", "8Vtkv2tdQ43bNGdWN9vNx9GVS9wrbXHk4ZW8kmucPmaYJwwedXir52kti9wJhcik4HehyqgLrQ1hBuirviLhxgRBNv", "hello world"}
// SHA3 not yet implemented in standard Java library. https://docs.oracle.com/javase/7/docs/technotes/guides/security/StandardNames.html#MessageDigest
//            {Multihash.Type.sha3_512, "SHA3-512", "8tWhXW5oUwtPd9d3FnjuLP1NozN3vc45rmsoWEEfrZL1L6gi9dqi1YkZu5iHb2HJ8WbZaaKAyNWWRAa8yaxMkGKJmX", "hello world"}
        };

        for(Object[] ex: examples) {
            Multihash m = Multihash.fromBase58((String)ex[2]);
            try {
                MessageDigest md = MessageDigest.getInstance((String) ex[1]);
                assert(md != null);
                md.update(((String) ex[3]).getBytes("UTF-8"));
                byte[] digest = md.digest();
                // Test constructor
                Multihash m2 = new Multihash((Multihash.Type)ex[0], digest);
                // Test comparison
                assert(m2.equals(m));
                // Test conversions
                assert(m.toBase58().equals(m2.toBase58()));
                assert(m.toBase58().equals((String)ex[2]));
            }
            catch (Exception e){
                // Usually because a hash function not supported
                System.out.println(e.getMessage());
                assert(false);
            }
        }
    }
}
