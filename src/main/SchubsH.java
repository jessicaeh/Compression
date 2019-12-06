// Program: SchubsH
// Author: Jessica Halbert
// Date: 12/3/19
// Execute: java schubsH <filename> or java schubsH <GLOB>
// Purpose: Compress the given file(s) <filename> and produce <filename>.hh for Huffman

import java.io.IOException;
import java.io.File;

public class SchubsH
{
    // alphabet size of extended ASCII
    private static final int R = 256;

    public static void main(String[] args)
    {
        for (int i = 0; i < args.length; i++)
            compress(args[i], args[i] + ".hh");
    }

    // Huffman trie node
    private static class Node implements Comparable<Node>
    {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right)
        {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        // checks if the node is a leaf node
        private boolean isLeaf()
        {
            assert (left == null && right == null) || (left != null && right != null);
            return (left == null && right == null);
        }

        // compares based on frequency
        public int compareTo(Node that)
        {
            return this.freq - that.freq;
        }
    }

    // compresses bytes from file and writes to file
    public static void compress(String filename, String filename2)
    {
        // we're using BinaryIn so we can read a file instead of stdin
        BinaryIn reader = new BinaryIn(filename);
        BinaryOut writer = new BinaryOut(filename2);

        // read the input file
        String s = reader.readString();
        char[] input = s.toCharArray();

        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;

        Node root = buildTrie(freq);

        String[] st = new String[R];
        buildCode(st, root, "");

        writeTrie(root, writer);

        writer.write(input.length);

        for (int i = 0; i < input.length; i++)
        {
            String code = st[input[i]];

            for (int j = 0; j < code.length(); j++)
            {
                if (code.charAt(j) == '0')
                    writer.write(false);
                else if (code.charAt(j) == '1')
                    writer.write(true);
                else
                    throw new RuntimeException("Illegal state");
            }
        }

        writer.close();
    }

    // build the Huffman trie given frequencies
    private static Node buildTrie(int[] freq)
    {
        // initialize priority queue with singleton trees
        MinPQ<Node> pq = new MinPQ<Node>();
        for (char i = 0; i < R; i++)
            if (freq[i] > 0)
                pq.insert(new Node(i, freq[i], null, null));

        // merge two smallest trees
        while (pq.size() > 1)
        {
            Node left  = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }

        return pq.delMin();
    }

    // write bitstring-encoded trie to file
    private static void writeTrie(Node x, BinaryOut writer)
    {
        if (x.isLeaf())
        {
            writer.write(true);
            writer.write(x.ch);
            return;
        }

        writer.write(false);

        writeTrie(x.left, writer);
        writeTrie(x.right, writer);
    }

    // make a lookup table from symbols and their encodings
    private static void buildCode(String[] st, Node x, String s)
    {
        if (!x.isLeaf())
        {
            buildCode(st, x.left,  s + '0');
            buildCode(st, x.right, s + '1');
        }
        else
            st[x.ch] = s;
    }

    // expands Huffman-encoded input from file and writes to file
    public static void expand(String filename, String filename2)
    {
        BinaryIn reader = new BinaryIn(filename);
        BinaryOut writer = new BinaryOut(filename2);

        Node root = readTrie(reader);

        int length = reader.readInt();

        for (int i = 0; i < length; i++)
        {
            Node x = root;

            while (!x.isLeaf())
            {
                boolean bit = reader.readBoolean();
                if (bit) x = x.right;
                else     x = x.left;
            }

            writer.write(x.ch);
        }

        writer.close();
    }

    private static Node readTrie(BinaryIn reader)
    {
        boolean isLeaf = reader.readBoolean();

        if (isLeaf)
        {
            char x = reader.readChar();
            return new Node(x, -1, null, null);
        }
        else
            return new Node('\0', -1, readTrie(reader), readTrie(reader));
    }
}