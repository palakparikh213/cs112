package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
    
    // prevent instantiation
    private Trie() { }
    
    /**
     * Builds a trie by inserting all words in the input array, one at a time,
     * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
     * The words in the input array are all lower case.
     * 
     * @param allWords Input array of words (lowercase) to be inserted.
     * @return Root of trie with all words inserted from the input array
     */
    public static TrieNode buildTrie(String[] allWords) {
        /** COMPLETE THIS METHOD **/
           TrieNode root = new TrieNode(null, null, null);
           if(allWords.length == 0)
               return root; //if no words, returns null tree
           root.firstChild = new TrieNode(new Indexes(0,(short)(0),(short)(allWords[0].length() - 1)), null, null);
           TrieNode pointer = root.firstChild, prev = root.firstChild;
           int s = -1, startIndex = -1, endIndex = -1, wordIndex = -1;
           for(int i = 1; i < allWords.length; i++) {
               String word = allWords[i];
                   while(pointer != null) {
                   startIndex = pointer.substr.startIndex;
                   endIndex = pointer.substr.endIndex;
                   wordIndex = pointer.substr.wordIndex;
                   if(startIndex > word.length()) {
                       prev = pointer;
                       pointer = pointer.sibling;
                       continue;
                   }
                   s = common(allWords[wordIndex].substring(startIndex, endIndex+1),
                           word.substring(startIndex));
                   if(s != -1)
                       s += startIndex;
                   if(s == -1) {
                       prev = pointer;
                       pointer = pointer.sibling;
                   }
                   else {
                       if(s == endIndex) {
                           prev = pointer;
                           pointer = pointer.firstChild;
                       }
                       else if (s < endIndex){
                           prev = pointer;
                           break;
                       }
                   }
               }
               if(pointer == null) {
                   Indexes alpha = new Indexes(i, (short)startIndex, (short)(word.length()-1));
                   prev.sibling = new TrieNode(alpha, null, null);
               } else {
                   Indexes currentIndexes = prev.substr;
                   TrieNode currentFirstChild = prev.firstChild;
                   Indexes currWordNewIndexes = new Indexes(currentIndexes.wordIndex, (short)(s+1), currentIndexes.endIndex);
                   currentIndexes.endIndex = (short)s;
                   prev.firstChild = new TrieNode(currWordNewIndexes, null, null);
                   prev.firstChild.firstChild = currentFirstChild;
                   prev.firstChild.sibling = new TrieNode(new Indexes((short)i,
                           (short)(s+1),
                           (short)(word.length()-1)),
                           null, null);
               }
              
               pointer = prev = root.firstChild;
               s = startIndex = endIndex = wordIndex = -1;
           }
          
           return root;
          
       }
       private static int common(String alpha, String beta){
           int ans = 0;
           while(ans<alpha.length() && ans<beta.length() && alpha.charAt(ans)==beta.charAt(ans)){
               ans++;
           }
           return (ans-1);
       }
        // FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
        // MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION

    
    /**
     * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
     * trie whose words start with this prefix. 
     * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
     * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
     * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
     * and for prefix "bell", completion would be the leaf node that holds "bell". 
     * (The last example shows that an input prefix can be an entire word.) 
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
         if(root==null){
               return null;
           } 
           ArrayList<TrieNode> answer = new ArrayList<TrieNode>();
           TrieNode pointer = root;
           while(pointer!=null){
               if(pointer.substr==null){
                   pointer = pointer.firstChild;
               }
           String s = allWords[pointer.substr.wordIndex];
           String alpha = s.substring(0, pointer.substr.endIndex+1);
           if(s.startsWith(prefix) || prefix.startsWith(alpha)){
               if(pointer.firstChild!=null){
                   answer.addAll(completionList(pointer.firstChild,allWords,prefix));
                   pointer=pointer.sibling;
               }else{
                   answer.add(pointer);
                   pointer=pointer.sibling;
               }
           }else{
               pointer = pointer.sibling;
           }
           return answer;
       }
        // FOLLOWING LINE IS A PLACEHOLDER TO ENSURE COMPILATION
        // MODIFY IT AS NEEDED FOR YOUR IMPLEMENTATION
        return null;
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
        
        for (TrieNode pointer=root.firstChild; pointer != null; pointer=pointer.sibling) {
            for (int i=0; i < indent-1; i++) {
                System.out.print("    ");
            }
            System.out.println("     |");
            print(pointer, indent+1, words);
        }
    }
 }
