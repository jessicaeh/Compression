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


## Design:
SchubsH uses the Huffman compression algorithm, while SchubsL uses the LZW compression algorithm. Deschubs expands files and can do files compressed using Huffman or LZW. SchubsArc makes a compressed archive of several files and Deschubs can be used to expand this archive back into its individual parts.

Huffman builds a trie and uses that to encode the file. When expanding, Huffman reads the trie used to encode the file and uses it to decode. LZW finds the longest string in the symbol table and writes the 8-bit value that corresponds. It scans one character past the longest string, then associates the next codeword value in the symbol table.


## Tests:
#### testOneSchubsH:
Make sure we can successfully SchubsH and Deschubs one random file

#### testAllSchubsH:
Make sure we can successfully SchubsH multiple files

#### testOneSchubsL:
Make sure we can successfully SchubsL and Deschubs one random file

#### testAllSchubsL:
Make sure we can successfully SchubsL multiple files

#### testSchubsArc:
Make sure we can successfully make an archive of multiple files tar'ed together and Deschubs them
