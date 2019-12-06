// Program: CompressionTest
// Author: Jessica Halbert
// Date: 12/4/19
// Run: mvn test
// Purpose: To test compression

import org.junit.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.commons.io.FileUtils;

public class CompressionTest extends TestCase
{
    // make sure we can successfully SchubsH and Deschubs one random file
    @Test
    public void testOneSchubsH() throws IOException
    {
        String randomFile = "src" + File.separator + "resources" + File.separator + "random.txt";
        String randomRenamed = "src" + File.separator + "resources" + File.separator + "randomRenamed.txt";
        File one = new File(randomFile);
        File two = new File(randomRenamed);

        // create a short .txt file with some random text in it
        createRandomFile(randomFile);

        // compress the random file
        SchubsH huffman = new SchubsH();
        huffman.main(new String[] {randomFile});

        // rename original random.txt so it's not overwritten when we Deschubs
        one.renameTo(two);

        // expand the compressed file
        Deschubs expand = new Deschubs();
        expand.main(new String[] {randomFile + ".hh"});

        // compare the two files
        boolean match = filesSame(randomFile, randomRenamed);

        // delete the files
        File three = new File(randomFile + ".hh");
        one.delete();
        two.delete();
        three.delete();

        // make sure the two files match
        assertEquals(match, true);
    }

    // make sure we can successfully SchubsH multiple files
    @Test
    public void testAllSchubsH() throws IOException
    {
        // prove the duplicates in schubsH actually match so we can test with them
        String folderPath = "src" + File.separator + "resources" + File.separator + "schubsH";
        same(folderPath);

        String abra = folderPath + File.separator + "abra.txt";
        String gosia = folderPath + File.separator + "gosia.txt";
        String input = folderPath + File.separator + "input.txt";

        // compress all the .txt files in src/resources/schubsH
        SchubsH huffman = new SchubsH();
        huffman.main(new String[] {abra, gosia, input});

        // delete the original files
        File one = new File(abra);
        File two = new File(gosia);
        File three = new File(input);
        one.delete();
        two.delete();
        three.delete();

        // expand the compressed files
        Deschubs expand = new Deschubs();
        expand.main(new String[] {abra + ".hh"});
        expand.main(new String[] {gosia + ".hh"});
        expand.main(new String[] {input + ".hh"});

        // compare all the files
        boolean match = filesSame(abra, folderPath + File.separator + "abra2.txt") && filesSame(gosia, folderPath + File.separator + "gosia2.txt") &&
                        filesSame(input, folderPath + File.separator + "input2.txt");

        // delete the compressed files
        File oneH = new File(abra + ".hh");
        File twoH = new File(gosia + ".hh");
        File threeH = new File(input + ".hh");
        oneH.delete();
        twoH.delete();
        threeH.delete();

        // make sure the files all expanded correctly
        assertEquals(match, true);
    }

    // make sure we can successfully SchubsL and Deschubs one random file
    @Test
    public void testOneSchubsL() throws IOException
    {
        String randomFile = "src" + File.separator + "resources" + File.separator + "random.txt";
        String randomRenamed = "src" + File.separator + "resources" + File.separator + "randomRenamed.txt";
        File one = new File(randomFile);
        File two = new File(randomRenamed);

        // create a short .txt file with some random text in it
        createRandomFile(randomFile);

        // compress the random file
        SchubsL lzw = new SchubsL();
        lzw.main(new String[] {randomFile});

        // rename original random.txt so it's not overwritten when we Deschubs
        one.renameTo(two);

        // expand the compressed file
        Deschubs expand = new Deschubs();
        expand.main(new String[] {randomFile + ".ll"});

        // compare the two files
        boolean match = filesSame(randomFile, randomRenamed);

        // delete the files
        File three = new File(randomFile + ".ll");
        one.delete();
        two.delete();
        three.delete();

        // make sure the two files match
        assertEquals(match, true);
    }

    // make sure we can successfully SchubsL multiple files
    @Test
    public void testAllSchubsL() throws IOException
    {
        // prove the duplicates in schubsL actually match so we can test with them
        String folderPath = "src" + File.separator + "resources" + File.separator + "schubsL";
        same(folderPath);

        String abra = folderPath + File.separator + "abra.txt";
        String gosia = folderPath + File.separator + "gosia.txt";
        String input = folderPath + File.separator + "input.txt";

        // compress all the .txt files in src/resources/schubsH
        SchubsL lzw = new SchubsL();
        lzw.main(new String[] {abra, gosia, input});

        // delete the original files
        File one = new File(abra);
        File two = new File(gosia);
        File three = new File(input);
        one.delete();
        two.delete();
        three.delete();

        // expand the compressed files
        Deschubs expand = new Deschubs();
        expand.main(new String[] {abra + ".ll"});
        expand.main(new String[] {gosia + ".ll"});
        expand.main(new String[] {input + ".ll"});

        // compare all the files
        boolean match = filesSame(abra, folderPath + File.separator + "abra2.txt") && filesSame(gosia, folderPath + File.separator + "gosia2.txt") &&
                        filesSame(input, folderPath + File.separator + "input2.txt");

        // delete the compressed files
        File oneH = new File(abra + ".ll");
        File twoH = new File(gosia + ".ll");
        File threeH = new File(input + ".ll");
        oneH.delete();
        twoH.delete();
        threeH.delete();

        // make sure the files all expanded correctly
        assertEquals(match, true);
    }

    // make sure we can successfully make an archive of multiple files tar'ed together and Deschubs them
    @Test
    public void testSchubsArc() throws IOException
    {
        // prove the duplicates in schubsL actually match so we can test with them
        String folderPath = "src" + File.separator + "resources" + File.separator + "schubsL";
        same(folderPath);

        String abra = folderPath + File.separator + "abra.txt";
        String gosia = folderPath + File.separator + "gosia.txt";
        String input = folderPath + File.separator + "input.txt";
        String archiveName = folderPath + File.separator + "archive";

        // make an archive of the .txt files in schubsL
        SchubsArc arc = new SchubsArc();
        arc.main(new String[] {"archive", abra, gosia, input});

        // delete the original files
        File one = new File(abra);
        File two = new File(gosia);
        File three = new File(input);
        one.delete();
        two.delete();
        three.delete();

        // expand the compressed file
        Deschubs expand = new Deschubs();
        expand.main(new String[] {archiveName + ".zl"});

        // compare all the files
        boolean match = filesSame(abra, folderPath + File.separator + "abra2.txt") && filesSame(gosia, folderPath + File.separator + "gosia2.txt") &&
                        filesSame(input, folderPath + File.separator + "input2.txt");

        // delete the compressed file
        File file = new File(archiveName + ".zl");
        file.delete();
        
        assertEquals(match, true);
    }

    public void same(String folderPath) throws IOException
    {
        assertEquals(filesSame(folderPath + File.separator + "abra.txt", folderPath + File.separator + "abra2.txt") &&
                    filesSame(folderPath + File.separator + "gosia.txt", folderPath + File.separator + "gosia2.txt") &&
                    filesSame(folderPath + File.separator + "input.txt", folderPath + File.separator + "input2.txt"), true);
    }

    // makes a txt file with the given name and fills it with some random numbers/characters for testing purposes
    public void createRandomFile(String filename) throws IOException
    {
        try
        {
            // prepare to write to file
            File file = new File(filename);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            // generate some short random text
            for (int i = 0; i < 5; i++)
            {
                String line = "";

                for (int j = 0; j < 10; j++)
                {
                    int ascii = ThreadLocalRandom.current().nextInt(33, 123);
                    line += (char)ascii;
                }

                // Write random tedt in file
                bw.write(line + "\n");
            }

            // Close connection
            bw.close();
        }
        catch(Exception e){
            System.err.println("Error generating random file");
        }
    }

    // checks to see if two files are the same
    public boolean filesSame(String input, String output) throws IOException
    {
        BufferedReader reader1 = new BufferedReader(new FileReader(input));
        BufferedReader reader2 = new BufferedReader(new FileReader(output));

        String line1 = reader1.readLine();
        String line2 = reader2.readLine();

        boolean areEqual = true;
        int lineNum = 1;

        while (line1 != null || line2 != null)
        {
            if(line1 == null || line2 == null)
            {
                areEqual = false;
                break;
            }
            else if(!line1.equalsIgnoreCase(line2))
            {
                areEqual = false;
                break;
            }

            line1 = reader1.readLine();
            line2 = reader2.readLine();
            lineNum++;
        }

        reader1.close();
        reader2.close();

        return areEqual;
    }
}
