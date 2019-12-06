// Program: SchubsL
// Author: Jessica Halbert
// Date: 12/3/19
// Execute: java schubsL <filename> or java schubsL <GLOB>
// Purpose: Compress the given file(s) <filename> and produce <filename>.ll for LZW

import java.io.IOException;
import java.io.File;

public class SchubsL
{
    // num of input chars
    private static final int R = 256;

    // num of codewords = 2^W
    private static final int L = 4096;

    // width of codeword
    private static final int W = 12;

    public static void main(String[] args)
    {
        for (int i = 0; i < args.length; i++)
            compress(args[i], args[i] + ".ll");
    }

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
            String s = st.longestPrefixOf(input);

            writer.write(st.get(s), W);
            int t = s.length();

            if (t < input.length() && code < L)
                st.put(input.substring(0, t + 1), code++);

            input = input.substring(t);
        }

        writer.write(R, W);
        writer.close();
    } 

    public static void expand(String filename, String filename2)
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
}