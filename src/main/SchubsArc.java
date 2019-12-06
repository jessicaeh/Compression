// Program: SchubsArc
// Author: Jessica Halbert
// Date: 12/3/19
// Execute: java schubsArc archive.zl *.txt
// Purpose: Produce one archive.zl containing compressed versions of all the txt files

import java.io.IOException;
import java.io.File;

public class SchubsArc
{
    // num of input chars
    private static final int R = 256;

    // num of codewords = 2^W
    private static final int L = 4096;

    // width of codeword
    private static final int W = 12;

    public static void main(String[] args) throws IOException
    {
        // args[0] = name of archive file
        // args[1...] = files to be archived

        // First, Tar everything together into src/resources/temp.tar
        String[] argsCopy = args.clone();
        argsCopy[0] = "src" + File.separator + "resources" + File.separator + "temp.tar";

        Tarsn tarser = new Tarsn();
        tarser.main(argsCopy);

        // Now we want to compress the tar file using LZW
        compress("src" + File.separator + "resources" + File.separator + "temp.tar",
                 "src" + File.separator + "resources" + File.separator + "schubsL" + File.separator + args[0] + ".zl");

        // get rid of the tar file since we don't need it anymore
        File tempTar = new File("src" + File.separator + "resources" + File.separator + "temp.tar");
        tempTar.delete();
    }

    // compress the tar file using LZW
    public static void compress(String filename, String filename2)
    { 
        // we're using BinaryIn so we can read a file instead of stdin
        BinaryIn reader = new BinaryIn(filename);
        BinaryOut writer = new BinaryOut(filename2);

        String input = reader.readString();
        TST<Integer> st = new TST<Integer>();

        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);

        int code = R + 1;

        while (input.length() > 0)
        {
            // Find max prefix match s
            String s = st.longestPrefixOf(input);

            // Print s's encoding
            writer.write(st.get(s), W);
            int t = s.length();

            // Add s to symbol table
            if (t < input.length() && code < L)
                st.put(input.substring(0, t + 1), code++);

            // Scan past s in input
            input = input.substring(t);
        }

        writer.write(R, W);
        writer.close();
    } 
}