import java.util.*;

/**
 * Class that builds HuffmanTree for PS-3;
 *
 * @author Jason Pak and Perry Zhang, Dartmouth CS 10, Fall 2020
 */

public class HuffmanTree {
    private BinaryTree<Node> ht;
    private Map<Character, String> codes;

    public HuffmanTree(Map<Character, Integer> freq) {

        Comparator<BinaryTree<Node>> comp = new TreeComparator();
        PriorityQueue<BinaryTree<Node>> pq = new PriorityQueue<BinaryTree<Node>>(comp);
        Set<Character> keys = freq.keySet();

        //put initial trees into priority queue
        for (Character c : keys) {
            pq.add(new BinaryTree<Node>(new Node(c, freq.get(c))));
        }

        //tree creation
        while(pq.size() > 1) {
            BinaryTree<Node> t1 = pq.remove();
            BinaryTree<Node> t2 = pq.remove();
            BinaryTree<Node> newTree = new BinaryTree<Node>(new Node(t1.getData().getFreq() + t1.getData().getFreq()), t1, t2);
            pq.add(newTree);
        }

        //removes the final (Huffman) tree in pq
        if(pq.size() != 0) {
            ht = pq.remove();

            //calls method to generate a Map from characters to code words
            codes = toMap();
        }
    }

    /**
     * Builds map with character as the key and the code as the value by traversing the Huffman Tree
     */
    public Map<Character, String> toMap() {
        Map<Character, String> toReturn = new HashMap<Character, String>();

        //handling boundary case for single character files
        if(ht.size() == 1) {
            //moves node from a root to a leaf
            ht = new BinaryTree<Node>(new Node(ht.getData().getFreq()), ht, null);
        }

        addToMap(ht, "", toReturn);
        return toReturn;
    }

    /**
     * Helper method for toMap, adding character (Key) & code (Value) to the Map
     */
    private void addToMap(BinaryTree<Node> tree, String code, Map<Character, String> codeMap) {
        if (tree.isLeaf()) {
            codeMap.put(tree.data.getChar(), code);
        }
        if(tree.hasLeft()) {
            addToMap(tree.getLeft(),code + "0", codeMap);
        }
        if(tree.hasRight()) {
            addToMap(tree.getRight(),code + "1", codeMap);
        }
    }

    /**
     * Getter method for final Huffman Tree
     */
    public BinaryTree<Node> getHT() {
        return ht;
    }

    /**
     * Getter method for Map storing the Huffman code for each character
     */
    public Map<Character, String> getCodes() {
        return codes;
    }

    /**
     * New class that is the data type in the binary tree
     * Allows for the storing of character and its frequency
     */
    public class Node {
        protected char c; //team name
        protected int freq; //team score

        public Node(int freq) {
            this.freq = freq;
        }

        public Node(char c) {
            this.c = c;
            this.freq = 1;
        }

        public Node(char c, int freq) {
            this.c = c;
            this.freq = freq;
        }

        public char getChar() {
            return c;
        }

        public int getFreq() {
            return freq;
        }

        @Override
        public String toString() {
            return c + ": " + freq;
        }
    }

    /**
     * Comparing frequency counts when comparing between two trees
     */
    private class TreeComparator implements Comparator<BinaryTree<Node>>  {
        @Override
        public int compare(BinaryTree<Node> o1, BinaryTree<Node> o2) {
            return o1.getData().getFreq() - o2.getData().getFreq();
        }
    }
}
