package nlp.datastructures.ngrams;

import java.util.HashMap;
import java.util.Map;

/**
* A generic type for n-grams
* @author Muti Kara
*/
public class NGram {
	
	Map<String, Integer> ngrams = new HashMap<>();
	int n;
	
	/**
	* takes a words string array (input string to be analized)
	* @param words
	* @param n
	 */
	public NGram(String[] words, int n){
        this.n = n;
		for (int i = n-1; i < words.length; i++) {
			String ngram = "";
			for (int j = n-1; j >= 0; j--) {
				ngram += words[i-j] + " ";
			}
			ngram = ngram.trim();
            ngrams.put(ngram, 1 + ((ngrams.containsKey(ngram)) ? ngrams.get(ngram) : 0));
        }
        ngrams.remove("");
        ngrams.remove(" ");
	}
	
	/**
	* 
	* @param str
	* @return how many times str occurs in the input array
	 */
	public int occurences(String str){
        if (ngrams.containsKey(str))
            return ngrams.get(str);
		return 0;
	}
	
	/**
	* 
	* @return a map from string to integer. 
	* integer value is the number of occurences of key string.
	 */
	public Map<String, Integer> getNgrams() {
		return ngrams;
	}
	
	public void print(){
        for (Map.Entry<String, Integer> entry : ngrams.entrySet()) {
			if(30*n - entry.getKey().length() > 0)
				System.out.println(entry.getKey() + "\t\t" + entry.getValue());
        }
	}
}
