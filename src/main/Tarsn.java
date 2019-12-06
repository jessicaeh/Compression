// Program: Tarsn
// Author: Jessica Halbert
// Date: 11/20/19
// Execute: java Tarsn archive-name file1 [file2...]
// Purpose: Converts a files to an archive and closes at the end

// java -cp target/classes BinaryDump 8 < src/resources/test1.txt
// java -cp target/classes BinaryDump 8 < src/resources/blee2.tar

// java -cp target/classes Tarsn src/resources/secondTest/allFour.tar src/resources/secondTest/*.txt

import java.io.IOException;
import java.io.File;

public class Tarsn
{
    public static void main(String[] args) throws IOException
    {
        File in1 = null;
        BinaryIn bin1 = null;
        BinaryOut out = null;

        char separator = (char) 255; // all ones 11111111

        for (int i = 1; i < args.length; i++)
        {
            try
            {
                // notice the input files start at arg[1], not arg[0]
                in1 = new File(args[i]);
                if (!in1.exists() || !in1.isFile()) return;

                long filesize = in1.length();
                int filenamesize = args[i].length();

                // archive file is at args[0]
                // layout: file-name-length, separator, filename, separator, file-size, separator, file
                out = new BinaryOut(args[0]);

                out.write(filenamesize);
                out.write(separator);

                out.write(args[i]);
                out.write(separator);

                out.write(filesize);
                out.write(separator);

                // writes the file
                bin1 = new BinaryIn(args[i]);

                while (!bin1.isEmpty())
                    out.write(bin1.readChar());
            }
            finally
            {
                if (out != null)
                    out.close();
            }
        }
    }
}