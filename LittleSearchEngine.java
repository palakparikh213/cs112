package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages in
 * which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {
    
    /**
     * This is a hash table of all keywords. The key is the actual keyword, and the associated value is
     * an array list of all occurrences of the keyword in documents. The array list is maintained in 
     * DESCENDING order of frequencies.
     */
    HashMap<String,ArrayList<Occurrence>> keywordsIndex;
    
    /**
     * The hash set of all noise words.
     */
    HashSet<String> noiseWords;
    
    /**
     * Creates the keyWordsIndex and noiseWords hash tables.
     */
    public LittleSearchEngine() {
        keywordsIndex = new HashMap<String,ArrayList<Occurrence>>(1000,2.0f);
        noiseWords = new HashSet<String>(100,2.0f);
    }
    
    /**
     * Scans a document, and loads all keywords found into a hash table of keyword occurrences
     * in the document. Uses the getKeyWord method to separate keywords from other words.
     * 
     * @param docFile Name of the document file to be scanned and loaded
     * @return Hash table of keywords in the given document, each associated with an Occurrence object
     * @throws FileNotFoundException If the document file is not found on disk
     */
    public HashMap<String,Occurrence> loadKeywordsFromDocument(String docFile) 
    throws FileNotFoundException {
        /** COMPLETE THIS METHOD **/
        //file will be scanned and loaded
        Scanner scan = new Scanner(new File(docFile));
        //creates hash table of keywords
        HashMap<String,Occurrence> keywords = new HashMap<String,Occurrence>();
        //if file exists
        if(scan != null) {
        //traverses file
        while(scan.hasNext()) {
            String keyword = scan.next();
            //create keyword
            keyword = getKeyword(keyword);
            //check if null
            if(keyword != null) {
                if(!keywords.containsKey(keyword)) {
                    Occurrence freq = new Occurrence(docFile,1);
                    keywords.put(keyword, freq);
                }else {
                    keywords.get(keyword).frequency++;
                }
            }
        }
        return keywords;
    }
    else {
        throw new FileNotFoundException();
    }

}
    
    /**
     * Merges the keywords for a single document into the master keywordsIndex
     * hash table. For each keyword, its Occurrence in the current document
     * must be inserted in the correct place (according to descending order of
     * frequency) in the same keyword's Occurrence list in the master hash table. 
     * This is done by calling the insertLastOccurrence method.
     * 
     * @param kws Keywords hash table for a document
     */
    public void mergeKeywords(HashMap<String,Occurrence> kws) {
        /** COMPLETE THIS METHOD **/
        //traverses kws 
        for(String s : kws.keySet()) {
            if(keywordsIndex.containsKey(s)) {
                ArrayList<Occurrence> masterList = keywordsIndex.get(s);
                masterList.add(kws.get(s));
                //inserts in correct place
                insertLastOccurrence(masterList);
                keywordsIndex.put(s, masterList);
                
            }
            else {
                ArrayList<Occurrence> masterList = new ArrayList<Occurrence>();
                masterList.add(kws.get(s));
                //inserts in correct place
                insertLastOccurrence(masterList);
                keywordsIndex.put(s, masterList);
            }
        }
    }
    
    /**
     * Given a word, returns it as a keyword if it passes the keyword test,
     * otherwise returns null. A keyword is any word that, after being stripped of any
     * trailing punctuation(s), consists only of alphabetic letters, and is not
     * a noise word. All words are treated in a case-INsensitive manner.
     * 
     * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!'
     * NO OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
     * 
     * If a word has multiple trailing punctuation characters, they must all be stripped
     * So "word!!" will become "word", and "word?!?!" will also become "word"
     * 
     * See assignment description for examples
     * 
     * @param word Candidate word
     * @return Keyword (word without trailing punctuation, LOWER CASE)
     */
    public String getKeyword(String word) {
        /** COMPLETE THIS METHOD **/
        //case insensitive
        word = word.toLowerCase();
        //traverses word and deletes punctuation before
        while(word.length()>= 1 && (!Character.isLetter(word.charAt(0)) || !Character.isLetter(word.charAt(word.length()-1)))) {
            if(word.length()>1) {
                if (!Character.isLetter(word.charAt(0))) {
                    word = word.substring(1);
                }
                else if(!Character.isLetter(word.charAt(word.length()-1))) {
                    word = word.substring(0,word.length()-1);
                }
            }else {
                word = "";
            }

        }
        //checks for punctuation at the end
        //while(word.length()>=1 && !Character.isLetter(word.charAt(word.length()-1))) {
            //if(word.length()>1) {
                //word = word.substring(0,word.length()-1);
            //}else {
                //word = "";
            //}    
        //check if word is left after deleting punctuation
        if(word.length()==0) {
            return null;
        }
        //now with what's left, check punctuation in between, traverse through word
        for(int i=0; i<word.length(); i++) {
            //check if letter
            if(!Character.isLetter(word.charAt(i))) {
                return null;
            }
        }
        if(noiseWords.contains(word)) {
            return null;
        }
        return word;
    }
    
    /**
     * Inserts the last occurrence in the parameter list in the correct position in the
     * list, based on ordering occurrences on descending frequencies. The elements
     * 0..n-2 in the list are already in the correct order. Insertion is done by
     * first finding the correct spot using binary search, then inserting at that spot.
     * 
     * @param occs List of Occurrences
     * @return Sequence of mid point indexes in the input list checked by the binary search process,
     *         null if the size of the input list is 1. This returned array list is only used to test
     *         your code - it is not used elsewhere in the program.
     */
    public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
        /** COMPLETE THIS METHOD **/
        //creates empty list of sequence of mid point indexes
        ArrayList<Integer> midPoints = new ArrayList<Integer>();
        Occurrence lastocc = occs.get(occs.size()-1);
        int freq = lastocc.frequency;
        int low = 0;
        int high = occs.size()-2;
        int middle = 0;
        while(low<=high) { //binary search
            middle = (low+high)/2;
            midPoints.add(middle);
            if(occs.get(middle).frequency<freq) {
                high = middle-1;
            }else if(occs.get(middle).frequency>freq) {
                low = middle+1;
            }else if(occs.get(middle).frequency == freq) {
                break;
            }
        }
        if(freq<=occs.get(middle).frequency) { 
            occs.add(middle+1, lastocc);
        }else {
            if(middle==0) {
                occs.add(0, lastocc);
            }else {
                occs.add(middle-1, lastocc);
            }
        }
        occs.remove(occs.size()-1);
        return midPoints;
    }
    
    /**
     * This method indexes all keywords found in all the input documents. When this
     * method is done, the keywordsIndex hash table will be filled with all keywords,
     * each of which is associated with an array list of Occurrence objects, arranged
     * in decreasing frequencies of occurrence.
     * 
     * @param docsFile Name of file that has a list of all the document file names, one name per line
     * @param noiseWordsFile Name of file that has a list of noise words, one noise word per line
     * @throws FileNotFoundException If there is a problem locating any of the input files on disk
     */
    public void makeIndex(String docsFile, String noiseWordsFile) 
    throws FileNotFoundException {
        // load noise words to hash table
        Scanner sc = new Scanner(new File(noiseWordsFile));
        while (sc.hasNext()) {
            String word = sc.next();
            noiseWords.add(word);
        }
        
        // index all keywords
        sc = new Scanner(new File(docsFile));
        while (sc.hasNext()) {
            String docFile = sc.next();
            HashMap<String,Occurrence> kws = loadKeywordsFromDocument(docFile);
            mergeKeywords(kws);
        }
        sc.close();
    }
    
    /**
     * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2 occurs in that
     * document. Result set is arranged in descending order of document frequencies. 
     * 
     * Note that a matching document will only appear once in the result. 
     * 
     * Ties in frequency values are broken in favor of the first keyword. 
     * That is, if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same 
     * frequency f1, then doc1 will take precedence over doc2 in the result. 
     * 
     * The result set is limited to 5 entries. If there are no matches at all, result is null.
     * 
     * See assignment description for examples
     * 
     * @param kw1 First keyword
     * @param kw1 Second keyword
     * @return List of documents in which either kw1 or kw2 occurs, arranged in descending order of
     *         frequencies. The result size is limited to 5 documents. If there are no matches, 
     *         returns null or empty array list.
     */
    public ArrayList<String> top5search(String kw1, String kw2) {
        /** COMPLETE THIS METHOD **/
        //creates list of results
        ArrayList<String> results = new ArrayList<String>();

        ArrayList<Occurrence> first = keywordsIndex.get(kw1);
        ArrayList <Occurrence> second = keywordsIndex.get(kw2);
        //intialize indexes and counter
        int i = 0; 
        int j = 0; 
        int count = 0; 
        //if lists are empty, return  null
        if(first == null && second == null) {
            return null;
        }
        //if first list is empty, add second to results
        else if(first == null) {
            while(j<second.size()&& count<5) {
                results.add(second.get(j).document);
                j++;
                count++;
            }
        }
        //if second list is empty, add first to results
        else if(second==null) {
            while(i<first.size()&& count<5) {
                results.add(first.get(i).document);
                i++;
                count++;
            }
        }else { //if neither are empty
            while(i<first.size() && j<second.size() && count<5){
                //higher frequency for first 
                if(first.get(i).frequency > second.get(j).frequency) {
                    if(!results.contains(first.get(i).document)) {
                        results.add(first.get(i).document);
                        count++;
                    }
                    i++;
                }
                //higher frequency for second
                else if(first.get(i).frequency < second.get(j).frequency) {
                    if(!results.contains(second.get(j).document)) {
                        results.add(second.get(j).document);
                        count++;
                    }
                    j++;
                }
                else { //tie goes to first
                    if(!results.contains(first.get(i).document)) {
                        results.add(first.get(i).document);
                        count++;
                        i++;
                    }else if(!results.contains(second.get(j).document)) {
                        results.add(second.get(j).document);
                        count++;
                        j++;
                    }
                    
                }
            }
            while((i<first.size() || j<second.size())&&count<5) {
                //if first is finished, we can go to second
                if(i==first.size()) {
                    if(!results.contains(second.get(j).document)) {
                        results.add(second.get(j).document);
                        count++;
                    }
                    j++;
                }else { //if we finish second, we can go to first
                    if(!results.contains(first.get(i).document)) {
                        results.add(first.get(i).document);
                        count++;
                    }
                    i++;
                }
            }
        }
        return results;
    
    
    }
}
