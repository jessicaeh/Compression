// Program: Deschubs
// Author: Jessica Halbert
// Date: 12/3/19
// Execute:  java deschubs <filename>.hh|ll or java deschubs <archive>.zz
// Purpose: Produce one archive.zz containing compressed versions of all the txt files

import java.io.File;

public class Deschubs
{
    // num of codewords = 2^W
    private static final int L = 4096;

    // width of codeword
    private static final int W = 12;

    // alphabet size of extended ASCII
    private static final int R = 256;

    public static void main(String[] args) 
    {
        // gets the .hh or .ll extension
        String extension = args[0].substring(args[0].length() - 3);

        if (extension.equals(".hh"))
            expandH(args[0], args[0].substring(0, args[0].length() - 3));
        else if (extension.equals(".ll"))
            expandL(args[0], args[0].substring(0, args[0].length() - 3));
        else if (extension.equals(".zl"))
            expandZL(args[0], args[0].substring(0, args[0].length() - 3) + ".tar");
        else
            throw new RuntimeException("This file type not supported.");
    }

    // Huffman Expansion --------------------------------------------------------------------------------------------------------

    public static void expandH(String filename, String filename2)
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
                if (bit)
                    x = x.right;
                else
                    x = x.left;
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

    // LZW Expansion --------------------------------------------------------------------------------------------------------

    public static void expandL(String filename, String filename2)
    {
        // we're using BinaryIn so we can read a file instead of stdin
        BinaryIn reader = new BinaryIn(filename);
        BinaryOut writer = new BinaryOut(filename2);

        String[] st = new String[L];

        int i;

        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;

        st[i++] = "";

        int codeword = reader.readInt(W);
        String val = st[codeword];

        while (true)
        {
            writer.write(val);
            codeword = reader.readInt(W);
            
            if (codeword == R)
                break;

            String s = st[codeword];

            if (i == codeword)
                s = val + val.charAt(0);

            if (i < L)
                st[i++] = val + s.charAt(0);

            val = s;
        }

        writer.close();
    }

    // LZW Tar Expansion ----------------------------------------------------------------------------------------------------

    public static void expandZL(String filename, String filename2)
    {
        // first, expand from .zl to .tar
        expandL(filename, filename2);

        // next, we'll untar the tar file
        try
        {
            Untars untarser = new Untars();
            untarser.main(new String[] {filename2});

            File tar = new File(filename2);
            tar.delete();
        }
        catch (Exception e)
        {
            System.err.println(e);
        }
        
    }
}