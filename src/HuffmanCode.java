import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanCode {

    private File file;
    private double[] frequencies;
    private PriorityQueue<Node> frequenciesTree;

    private HashMap<Byte, String> dictionary;
    private HashMap<String, Byte> reverseDictionary;

    /**
     * Works on a file only
     *
     * TODO: Make it work with just strings from the outside interface of the class
     * */

    public HuffmanCode(File file) {
        this.file = file;
        this.dictionary = new HashMap<>();
        this.reverseDictionary = new HashMap<>();

        this.frequencies = calculateFrequencies(file);
        this.frequenciesTree = buildHeap();


        // Start building the tree
        while (this.frequenciesTree.size() > 1) {
            Node lowest = this.frequenciesTree.poll();
            Node secondLowest = this.frequenciesTree.poll();

            Node parent = new Node((byte)0, lowest.frequency + secondLowest.frequency);
            parent.rightChild = lowest;
            parent.leftChild = secondLowest;

            this.frequenciesTree.add(parent);
        }

        // We're left with a heap with only 1 node in it as its root.
        // That node is the root of the Huffman Tree we created
        Node huffmanTree = this.frequenciesTree.peek(); // Take it

        calculateCodesForLeafs(huffmanTree); // put in each node the code for the bytes (01010...)

        // printLeaf(huffmanTree); // print the tree (post-order traversal)


        String compressed = encodeToString();
        System.out.println("Compressed : " + compressed);


        System.out.println();
        String decompressed = decode(compressed);

        System.out.println("Decompressed : " + decompressed);


        System.out.println("cost: " + calculateCost());
    }


    private String encodeToString() {
        StringBuilder encoded = new StringBuilder();


        try {
            FileInputStream fileInputStream = new FileInputStream(this.file);

            byte[] buffer = new byte[1024];
            int bytesRead = 0;

            while (bytesRead != -1) {
                bytesRead = fileInputStream.read(buffer);

                for (int i = 0; i < bytesRead; i++) {
                    byte currentByte = buffer[i];

                    System.out.println("Byte: " + ((char)currentByte) + " according to dictionary its: " + dictionary.get(currentByte));
                    encoded.append(dictionary.get(currentByte));


                }

            }

            fileInputStream.close();
            return encoded.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private double calculateCost() {
        int total = dictionary.size();

        double averageCost = 0.0;

        for (String i : dictionary.values()) {
            averageCost = averageCost + i.length();
        }

        System.out.println("size of dictionary: " + total + " total bits: " + averageCost);
        return averageCost/total;
    }

    public String decode(String encoded) {
        StringBuilder result = new StringBuilder();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < encoded.length(); i++) {
            char currentChar = encoded.charAt(i);

            stringBuilder.append(currentChar);

            Byte by = this.reverseDictionary.get(stringBuilder.toString());
            if (by != null) {

                stringBuilder.delete(0, stringBuilder.length());
                char asciChar = (char) (0+by);
                System.out.println("Byte: " + by + " ASCI : " + asciChar);
                result.append(asciChar);
            }
        }
        return result.toString();
    }


    private void printLeaf(Node node) {
        if (node.isLeaf()) {
            System.out.println(node);
        } else {

            if (node.leftChild != null) {
                printLeaf(node.leftChild);
            }
            if (node.rightChild != null) {
                printLeaf(node.rightChild);
            }

        }
    }



    private void calculateCodesForLeafs(Node rootOfHuffmanTree) {
        calculateCodesForLeafs(rootOfHuffmanTree.leftChild, "0");
        calculateCodesForLeafs(rootOfHuffmanTree.rightChild, "1");
    }

    private void calculateCodesForLeafs(Node rootOfHuffmanTree, String x) {
        rootOfHuffmanTree.appendToString(x);
        if (!rootOfHuffmanTree.isLeaf()) {
            if (rootOfHuffmanTree.leftChild != null) {
                calculateCodesForLeafs(rootOfHuffmanTree.leftChild, x.concat("0"));
            }

            if (rootOfHuffmanTree.rightChild != null) {
                calculateCodesForLeafs(rootOfHuffmanTree.rightChild, x.concat("1"));
            }
        } else {
            this.dictionary.put(rootOfHuffmanTree.sByte, rootOfHuffmanTree.code);
            this.reverseDictionary.put(rootOfHuffmanTree.code, rootOfHuffmanTree.sByte);
        }
    }


    private PriorityQueue<Node> buildHeap() {
        // Get frequencies, create array of nodes
        ArrayList<Node> nodeArrayList = new ArrayList<>(frequencies.length);

        for (int i = 0; i < frequencies.length; i++) {
            // i represents the current byte iterating
            if (frequencies[i] != 0.0) {
                Node node = new Node((byte) i, frequencies[i]); // Create the node with data
                nodeArrayList.add(node);
            }
        }


        return new PriorityQueue<>(nodeArrayList);
    }



    /**
     * This function calculates the frequencies of the bytes in the file and returns it in the form of an double array
     * of size 256 (Since there can only be 256 different bytes)
     *
     * @param file The non-null file
     * @return An double array of size 256, each index from 0 to 255 represent a byte, and the double value inside it is the frequency
     *
     * */
    public static double[] calculateFrequencies(File file) {



        double frequencies [] = new double[256];
        int occurrences = 0;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while (bytesRead != -1) {
                bytesRead = fileInputStream.read(buffer);
                for (int i = 0; i < bytesRead; i++) {
                    frequencies[buffer[i]] = frequencies[buffer[i]] + 1;

                    occurrences++;
                }
            }
            fileInputStream.close();

            // Done with reading file, calculate frequencies
            for (int i = 0; i < frequencies.length; i++) {
                frequencies[i] = frequencies[i] / occurrences;
                if (frequencies[i] != 0) {
                    System.out.println("Char: " + ((char) i) + ":" + frequencies[i]);

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            frequencies = null;
        }
        return frequencies;
    }


    public static class Node implements Comparable<Node> {
        protected Node leftChild, rightChild, father;
        protected byte sByte = 0;
        protected double frequency = 0.0;

        private String code = "";

        public Node(byte sByte, double frequency) {
            this.sByte = sByte;
            this.frequency = frequency;
        }

        public void appendToString(String x) {
            // Only acceptable int is 0 or 1
            this.code = this.code.concat(x+ " ");
        }

        public boolean isLeaf() {
            return this.leftChild == null && this.rightChild == null;
        }

        @Override
        public int compareTo(Node o) {
            return this.frequency > o.frequency ? 1 : -1;
        }

        @Override
        public String toString() {
            return "Byte: " + sByte + " :: frequency: " + this.frequency + " :: code: " + code + " hash: " + this.hashCode();
        }
    }
}
