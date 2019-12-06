## Design:
SchubsH uses the Huffman compression algorithm, while SchubsL uses the LZW compression algorithm. Deschubs expands files and is capable of expanding files compressed using Huffman or LZW. SchubsArc makes a compressed archive of several files and Deschubs can be used to expand this archive back into its individual parts.


## Tests:
testOneSchubsH: make sure we can successfully SchubsH and Deschubs one random file

testAllSchubsH: make sure we can successfully SchubsH multiple files

testOneSchubsL: make sure we can successfully SchubsL and Deschubs one random file

testAllSchubsL: make sure we can successfully SchubsL multiple files

testSchubsArc: make sure we can successfully make an archive of multiple files tar'ed together and Deschubs them


## Installation Instructions:
git clone


## Maven Instructions:
mvn clean

mvn compile

mvn test


## CLI Instructions:
#### SchubsH:
> java -cp target/classes SchubsH (filename)

or

> java -cp target/classes SchubsH (GLOB)


#### SchubsL:

> java -cp target/classes SchubsL (filename)

or

> java -cp target/classes SchubsL (GLOB)


#### Deschubs:

> java -cp target/classes Deschubs (filename).hh|ll

or

> java -cp target/classes Deschubs (GLOB).zl


#### SchubsArc:

> java -cp target/classes SchubsArc (name of archive) (filename)

or

> java -cp target/classes SchubsArc (name of archive) (GLOB)
