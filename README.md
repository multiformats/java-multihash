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
