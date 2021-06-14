import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Driver for PS-3; Takes pathName of text file as an input and outputs a compressed and decompressed text file.
 *
 * @author Jason Pak and Perry Zhang, Dartmouth CS 10, Fall 2020
 */

public class FileOperations {

    public static void main(String[] args) {

        Map<Character, Integer> frequencyTable = new HashMap<Character, Integer>();
        Map<Character, String> codes;
        BufferedReader input;

        /* Uncomment "pathName" variable depending on which txt file to test: */

        //String pathName = "inputs/NotExistentFileTest.txt";
        //String pathName = "inputs/EmptyFileTest.txt";
        //String pathName = "inputs/SingleCharTest.txt";
        //String pathName = "inputs/SingleCharRepeatedTest.txt";
        //String pathName = "inputs/RandomTest.txt";
        
        String pathName = "inputs/WarAndPeace.txt";
        //String pathName = "inputs/USConstitution.txt";

        //try opening file, catch if file doesn't exist
        try {
            input = new BufferedReader(new FileReader(pathName));
        }
        catch (FileNotFoundException e) {
            System.err.println("Cannot open file\n" + e.getMessage());
            return;
        }

        //try reading file, catch any errors
       try {
           int line;

           while ((line = input.read()) != -1) {
               char curr = (char)line;

               //generating frequency map
               if(frequencyTable.containsKey(curr)) {
                   int count = frequencyTable.get(curr);
                   frequencyTable.put(curr, count + 1);
               }
               else {
                   frequencyTable.put(curr, 1);
               }
           }

           //builds huffman tree
           HuffmanTree tree = new HuffmanTree(frequencyTable);

           //obtains map with character as key and code as the value
           codes = tree.getCodes();

           //reads input file and compresses
           input = new BufferedReader(new FileReader(pathName));
           String compressedPathName = pathName.substring(0, pathName.indexOf(".txt")) + "_compressed.txt";
           BufferedBitWriter bitOutput = new BufferedBitWriter(compressedPathName);

           while ((line = input.read()) != -1) {
               char curr = (char)line;
               String code = codes.get(curr);

               //writing compressed file
               for(int i = 0; i<code.length(); i++) {
                   if(code.substring(i,i+1).equals("0")) {
                       bitOutput.writeBit(false);
                   }
                   else {
                       bitOutput.writeBit(true);
                   }
               }
           }
           bitOutput.close();
           input.close();

           //reading compressed file and decompresses
           String decompressedPathName = pathName.substring(0, pathName.indexOf(".txt")) + "_decompressed.txt";
           BufferedWriter output = new BufferedWriter(new FileWriter(decompressedPathName));
           BufferedBitReader bitInput = new BufferedBitReader(compressedPathName);

           while (bitInput.hasNext()) {
               BinaryTree<HuffmanTree.Node> ht = tree.getHT();

               //writing decompressed file
               while (!ht.isLeaf()) {
                   boolean bit = bitInput.readBit();

                   if (!bit) { //0
                        ht = ht.getLeft();
                   } else { //1
                        ht = ht.getRight();
                   }
               }
               output.write(ht.getData().getChar());
           }
           bitInput.close();
           output.close();

       }
       catch (IOException e) {
           System.err.println("IO error while reading.\n" + e.getMessage());
       }

        // Close the file, if possible
        try {
            input.close();
        }
        catch (IOException e) {
            System.err.println("Cannot close file.\n" + e.getMessage());
        }
    }

}
