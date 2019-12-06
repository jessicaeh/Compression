// Program: DupCopyFolders
// Author: Jessica Halbert
// Date: 10/31/19
// Run: java DupCopyFolders.java
// Purpose: Copy from folders without packages

import java.io.*;

public class DupCopyFolders
{
    // command line arguments will be in args (source, destination)
    public static void main( String[] args ) throws IOException {
        File srcFile = new File(args[0]);
        File destFile = new File(args[1]);

        DupCopyFolders copier = new DupCopyFolders();
        copier.copy(srcFile, destFile);
    }

    public void copy(File copy, File paste) throws IOException
    {
        // if the file we're looking at is a folder, then call recursively so we can get to the bottom
        if(copy.isDirectory())
        {
            // if the new folder doesn't already exist, create it
            if(!paste.exists())
                paste.mkdir();

            // will store all of the files inside of the folder
            String files[] = copy.list();

            // recursively copies files inside of folders
            for (String file : files)
            {
                File source = new File(copy, file);
                File destination = new File(paste, file);

                copy(source, destination);
            }

        } // means we're looking at an actual file as opposed to a folder
        else
        {
            // copies the contents of the files
            InputStream in = new FileInputStream(copy);
            OutputStream out = new FileOutputStream(paste);

            byte[] buffer = new byte[1024];

            int length;
            while ((length = in.read(buffer)) > 0)
            {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
        }
    }
}
