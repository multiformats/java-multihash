package io.ipfs.multihash;

import io.ipfs.multibase.Base58;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MultihashTest {

  @Test
  void base58Test() {
    List<String> examples =
        Arrays.asList(
            "QmPZ9gcCEpqKTo6aq61g2nXGUhM4iCL3ewB6LDXZCtioEB",
            "QmatmE9msSfkKxoffpHwNLNKgwZG8eT9Bud6YoPab52vpy");
    for (String example : examples) {
      byte[] output = Base58.decode(example);
      String encoded = Base58.encode(output);
      Assertions.assertEquals(example, encoded);
    }
  }

  @Test
  void decodeTest() throws IOException {
    List<String> base58 =
        Arrays.asList(
            "QmPZ9gcCEpqKTo6aq61g2nXGUhM4iCL3ewB6LDXZCtioEB",
            "QmatmE9msSfkKxoffpHwNLNKgwZG8eT9Bud6YoPab52vpy");
    List<String> base36 =
        Arrays.asList(
            "kmue2y4illvr0m3lt8x6z8iwghtxlzdmkjh957p5rr5cdr9243ugc",
            "kmuia3qyasz2z5cnz848bag5n5wfc7gzi35cz2npamtmkyifd5anu");
    for (int i = 0; i < base58.size(); i++) {
      Multihash b58 = Multihash.decode(base58.get(i));
      Multihash b36 = Multihash.decode(base36.get(i));
      Assertions.assertEquals(b58, b36);
    }
  }

  @Test
  void multihashTest() {
    Object[][] examples =
        new Object[][] {
          {Multihash.Type.id, "ID", "13hC12xCn", "hello"},
          {Multihash.Type.id, "ID", "11", ""},
          {Multihash.Type.md5, "MD5", "fzhnUYo18W8ewDBzLuzLqc9Twp", "hello world"},
          {Multihash.Type.sha1, "SHA-1", "5drNu81uhrFLRiS4bxWgAkpydaLUPW", "hello world"},
          {
            Multihash.Type.sha2_256,
            "SHA-256",
            "QmaozNR7DZHQK1ZcU9p7QdrshMvXqWK6gpu5rmrkPdT3L4",
            "hello world"
          },
          {
            Multihash.Type.sha2_512,
            "SHA-512",
            "8Vtkv2tdQ43bNGdWN9vNx9GVS9wrbXHk4ZW8kmucPmaYJwwedXir52kti9wJhcik4HehyqgLrQ1hBuirviLhxgRBNv",
            "hello world"
          },
          {
            Multihash.Type.sha3_512,
            "SHA3-512",
            "8tWhXW5oUwtPd9d3FnjuLP1NozN3vc45rmsoWEEfrZL1L6gi9dqi1YkZu5iHb2HJ8WbZaaKAyNWWRAa8yaxMkGKJmX",
            "hello world"
          }
        };

    for (Object[] ex : examples) {
      try {
        byte[] digest;
        if (ex[0] != Multihash.Type.id) {
          MessageDigest md = MessageDigest.getInstance((String) ex[1]);
          Assertions.assertNotNull(md);
          md.update(((String) ex[3]).getBytes("UTF-8"));
          digest = md.digest();
        } else digest = ((String) ex[3]).getBytes("UTF-8");
        // Test constructor
        Multihash m2 = new Multihash((Multihash.Type) ex[0], digest);
        Multihash m = Multihash.fromBase58((String) ex[2]);
        // Test comparison
        Assertions.assertEquals(m, m2);
        // Test conversions
        Assertions.assertEquals(m.toBase58(), m2.toBase58());
        Assertions.assertEquals(m.toBase58(), ex[2]);
        // Test fromHex and toHex
        Multihash m3 = Multihash.fromHex(m.toHex());
        Assertions.assertEquals(m, m3);
      } catch (Exception e) {
        System.out.println(e.getMessage());
        Assertions.assertTrue(false);
      }
    }
  }
}
