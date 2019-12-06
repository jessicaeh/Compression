// Program: Untars
// Author: Jessica Halbert
// Date: 11/8/19
// Execute: java Untars archive-name
// Purpose: Converts from a tars file

// java -cp target/classes Untars src/resources/blee.tar

import java.io.IOException;
import java.io.File;

public class Untars
{
    public static void main(String[] args) throws IOException
    {
        BinaryIn in = null;
        BinaryOut out = null;

        char sep = (char) 255; // all ones 11111111
        
        in = new BinaryIn(args[0]);

        while (!in.isEmpty())
        {
            try
            {
                // reads the filename
                int filenamesize = in.readInt();
                sep = in.readChar();
                String filename = "";

                for (int i = 0; i < filenamesize; i++)
                    filename += in.readChar();

                sep = in.readChar();

                // reads the filesize (given in bytes, so i.e. 11 would mean 11 bytes)
                long filesize = in.readLong();
                sep = in.readChar();

                // System.out.println("Extracting file " + filename + " (" + filesize + ").");

                out = new BinaryOut(filename);
                for (int i = 0; i < filesize; i++)
                    out.write(in.readChar());
            }
            finally
            {
                if (out != null)
                    out.close();
            }
        }
    }
}