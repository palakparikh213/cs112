package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Palak Parikh palakparikh213@gmail.com pdp89
 *
 */
public class Trie {
    
    // prevent instantiation
    private Trie() { }
    
    /**
     * Builds a trie by inserting all words in the input array, one at a time,
     * in sequence FROM FIRST TO prev. (The sequence is IMPORTANT!)
     * The words in the input array are all lower case.
     * 
     * @param allWords Input array of words (lowercase) to be inserted.
     * @return Root of trie with all words inserted from the input array
     */
    public static TrieNode buildTrie(String[] allWords) {
        /** COMPLETE THIS METHOD **/
        
        // FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
        TrieNode root = new TrieNode (null,null,null); //creates null root node
 
        //returns null root node if input array is empty
        if (allWords==null) {
            return root;
        }

        
            //initializes first child
            Indexes ind = new Indexes(0, (short)(0), (short)(allWords[0].length()-1));
            root.firstChild = new TrieNode(ind,null,null);
            //initializes pointer
            TrieNode ptr = root.firstChild;
            //initializes previous node
            TrieNode prev = root.firstChild;
            
            int k = -1;
            int wordIndex = -1;
            int startIndex = -1;
            int endIndex = -1;
            
            //  String word = " ";
            
            for(int index = 1; index < allWords.length; index++) {
                String word = allWords[index];
            while(ptr != null) {
                //ends loop if pointer becomes null and nothing matches
                startIndex = ptr.substr.startIndex;
                endIndex = ptr.substr.endIndex;
                wordIndex = ptr.substr.wordIndex;

                //traversing while checking if startIndex<word.length
                if(startIndex > word.length()) {
                    prev = ptr;
                    ptr = ptr.sibling;
                    continue;
                }

                int p = 0;
            while((p < (allWords[wordIndex].substring(startIndex, endIndex+1)).length()) && p < (word.substring(startIndex).length()) && (allWords[wordIndex].substring(startIndex, endIndex+1)).charAt(p) == (word.substring(startIndex).charAt(p))) {
                p++;
            }
                k=p-1;
                if(k!= -1) {
                    k+= startIndex;
                }
                if(k== -1) {
                    prev = ptr;
                    ptr = ptr.sibling;
                }
                else {
                    if(k== endIndex) { 
                        prev = ptr;
                        ptr = ptr.firstChild;
                    }
                    else if (k < endIndex){ 
                        prev = ptr;
                        break;
                    }
                }
            }
            
            if(ptr == null) {
                Indexes indexes = new Indexes(index, (short)startIndex, (short)(word.length()-1));
                prev.sibling = new TrieNode(indexes, null, null);
            } 
            else {
                Indexes currentIndex = prev.substr; 
                TrieNode currentFS = prev.firstChild; 
                Indexes currentWordIndexes = new Indexes(currentIndex.wordIndex, (short)(k+1), currentIndex.endIndex);
                currentIndex.endIndex = (short)k;    
                prev.firstChild = new TrieNode(currentWordIndexes, null, null);
                prev.firstChild.firstChild = currentFS;
                prev.firstChild.sibling = new TrieNode(new Indexes((short)index, (short)(k+1), (short)(word.length()-1)), null, null);
            }
            ptr = prev = root.firstChild;
            k = startIndex = endIndex = wordIndex = -1;
        }
        return root;
    }

    
    /**
     * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
     * trie whose words start with this prefix. 
     * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
     * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
     * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
     * and for prefix "bell", completion would be the leaf node that holds "bell". 
     * (The prev example shows that an input prefix can be an entire word.) 
     * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
     * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
     *
     * @param root Root of Trie that stores all words to search on for completion lists
     * @param allWords Array of words that have been inserted into the trie
     * @param prefix Prefix to be completed with words in trie
     * @return List of all leaf nodes in trie that hold words that start with the prefix, 
     *             order of leaf nodes does not matter.
     *         If there is no word in the tree that has this prefix, null is returned.
     */
    public static ArrayList<TrieNode> completionList(TrieNode root,String[] allWords, String prefix) {
        /** COMPLETE THIS METHOD **/
        if (root==null) {
            return null;
        }
        ArrayList<TrieNode> matches = new ArrayList<>();
        TrieNode ptr = root;
        while (ptr != null) {
            if(ptr.substr == null) {
                ptr = ptr.firstChild;
            }
            
            String words = allWords[ptr.substr.wordIndex];
            String a = words.substring(0, ptr.substr.endIndex+1);
            
            if (words.startsWith(prefix) || prefix.startsWith(a)) {
                if(ptr.firstChild != null) { 
                    matches.addAll(completionList(ptr.firstChild, allWords, prefix));
                    ptr = ptr.sibling;
                } 
                else { 
                    matches.add(ptr);
                    ptr = ptr.sibling;
                }
            } 
            else {
                ptr = ptr.sibling;
            }
        }
        
        return matches;

    }
    
    public static void print(TrieNode root, String[] allWords) {
        System.out.println("\nTRIE\n");
        print(root, 1, allWords);
    }
    
    private static void print(TrieNode root, int indent, String[] words) {
        if (root == null) {
            return;
        }
        for (int i=0; i < indent-1; i++) {
            System.out.print("    ");
        }
        
        if (root.substr != null) {
            String pre = words[root.substr.wordIndex]
                            .substring(0, root.substr.endIndex+1);
            System.out.println("      " + pre);
        }
        
        for (int i=0; i < indent-1; i++) {
            System.out.print("    ");
        }
        System.out.print(" ---");
        if (root.substr == null) {
            System.out.println("root");
        } else {
            System.out.println(root.substr);
        }
        
        for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
            for (int i=0; i < indent-1; i++) {
                System.out.print("    ");
            }
            System.out.println("     |");
            print(ptr, indent+1, words);
        }
    }
 }
