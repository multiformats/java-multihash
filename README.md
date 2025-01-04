# java-multihash

[![](https://img.shields.io/badge/made%20by-Protocol%20Labs-blue.svg?style=flat-square)](http://ipn.io)
[![](https://img.shields.io/badge/project-multiformats-blue.svg?style=flat-square)](https://github.com/multiformats/multiformats)
[![](https://img.shields.io/badge/freenode-%23ipfs-blue.svg?style=flat-square)](https://webchat.freenode.net/?channels=%23ipfs)
[![Travis CI](https://img.shields.io/travis/multiformats/java-multihash.svg?style=flat-square&branch=master)](https://travis-ci.org/multiformats/java-multihash)
[![Release](https://jitpack.io/v/multiformats/java-multihash.svg)](https://jitpack.io/#multiformats/java-multihash)
[![](https://img.shields.io/badge/readme%20style-standard-brightgreen.svg?style=flat-square)](https://github.com/RichardLitt/standard-readme)

> A Java implementation of [multihash](https://github.com/multiformats/multihash).

## Install

Simply clone this repository.

## Usage

```java
Multihash b58 = Multihash.decode("QmatmE9msSfkKxoffpHwNLNKgwZG8eT9Bud6YoPab52vpy");
Multihash b36 = Multihash.decode("kmue2y4illvr0m3lt8x6z8iwghtxlzdmkjh957p5rr5cdr9243ugc");
```

Note that this library only decodes & encodes Multihashes, and does not actually include any implementations of the hash functions themselves.

Consumers of this library can use different implementations for different reasons. Here are a few possible implementation choices for each `Multihash.Type`:

* [JDK's `MessageDigest`](https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/security/MessageDigest.html) supports (at least) these:
  * md5 = MD5
  * sha1 = SHA-1
  * sha2-224 = SHA-224
  * sha2-256 = SHA-256
  * sha2-384 = SHA-384
  * sha2-512 = SHA-512
  * sha2-512-224 = SHA-512/224
  * sha2-512-256 = SHA-512/256
  * sha3_224 = SHA3-224
  * sha3_256 = SHA3-256
  * sha3_512 = SHA3-512
* [Google Guava's Hashing](https://github.com/google/guava/wiki/hashingexplained) offers [intentionally only](https://github.com/google/guava/issues/5990#issuecomment-2571350434) these:
  * md5 = `@Deprecated Hashing.md5()`
  * sha1 = `@Deprecated Hashing.sha1()`
  * sha2_256 = `Hashing.sha256()`
  * sha2_512 = `Hashing.sha512()`
  * murmur3 = `Hashing.murmur3_32_fixed()`
* [Google Tink](https://developers.google.com/tink/supported-key-types#mac)
  * AES-CMAC, see [Multicodec Issue #368](https://github.com/multiformats/multicodec/issues/368)

Please contribute an update to this list if you know of any other related libraries.

## Dependency

You can use this project by building the JAR file as specified below, or by using [JitPack](https://jitpack.io/#multiformats/java-multihash/) (also supporting Gradle, SBT, etc).

for Maven, you can add the follwing sections to your POM.XML:

```xml
  <repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
  </repositories>

  <dependencies>
    <dependency>
      <groupId>com.github.multiformats</groupId>
      <artifactId>java-multihash</artifactId>
      <version>$LATEST_VERSION</version>
    </dependency>
  </dependencies>
```

## Testing

`mvn test`

## Building

`mvn package` will build a JAR file with Maven dependency information.

## Releasing

The version number is specified in the `pom.xml` file and must be changed in order to be accurately reflected in the JAR file manifest. A git tag must be added in the format "vx.x.x" for JitPack to work.

## Maintainers

Captain: [@ianopolous](https://github.com/ianopolous).

## Contribute

Contributions welcome. Please check out [the issues](https://github.com/multiformats/java-multihash/issues).

Check out our [contributing document](https://github.com/multiformats/multiformats/blob/master/contributing.md) for more information on how we work, and about contributing in general. Please be aware that all interactions related to multiformats are subject to the IPFS [Code of Conduct](https://github.com/ipfs/community/blob/master/code-of-conduct.md).

Small note: If editing the README, please conform to the [standard-readme](https://github.com/RichardLitt/standard-readme) specification.

## License

[MIT](LICENSE) © 2015 Ian Preston
