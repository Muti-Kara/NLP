package nlp.ngram;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import nlp.stemming.PortersAlgorithm;
import nlp.datastructures.ngrams.*;

/**
 * N-Gram Manager
 * Required for creating proper input for n-grams.
 * Also uses them for counting occurences of a given string.
 * @author Muti Kara
 */
public class NGramManager {
    final double UNKNOWN_PROPORTION = .15;
    public final String UNKNOWN = "<UNK>";
	final int NUMBER_OF_NGRAMS = 4;

	NGram[] ngrams = new NGram[NUMBER_OF_NGRAMS];
	
    public Map<String, Double> kneserNey = new HashMap<>();
    
	String[] words;
    String text;
    int unknownCount = 0;
    int wordCount;

	/**
	* input text
	* @param text
	 */
    public NGramManager(String text) {
        this.text = text;
        tokenize();
		ngrams[0] = new Unigram(words);
		ngrams[1] = new Bigram(words);
		ngrams[2] = new Trigram(words);
		ngrams[3] = new Quadgram(words);
		for(Map.Entry<String, Integer> entry : ngrams[1].getNgrams().entrySet()){
			String secondWord = entry.getKey().substring(entry.getKey().indexOf(' ') + 1);
            kneserNey.put(secondWord, 1 + ((kneserNey.containsKey(secondWord)) ? kneserNey.get(secondWord) : 0));
		}
		for(Map.Entry<String, Double> entry : kneserNey.entrySet()){
            kneserNey.put(entry.getKey(), kneserNey.get(entry.getKey()) / ngrams[1].getNgrams().size());
		}
        System.out.println("Tokenizing is done");
    }

    /**
     * This method will create nGram map from text
     */
    public void tokenize() {
        Random rand = new Random();
        text = text.replaceAll("[\n]", " ");
        text = text.replaceAll("[^A-Za-z ]", "");
        text = text.toLowerCase();
        words = text.split(" ");
        int len = words.length;

        System.out.println("There are " + len + " words.");

        for (int i = 0; i < len; i++)
            words[i] = words[i].trim();

        for (int i = 0; i < len; i++) {
            PortersAlgorithm pa = new PortersAlgorithm(words[i]);
            words[i] = pa.stemming();
        }
		
		
        for (int i = 0; i < len; i++) {
            kneserNey.put(words[i], 1 + ((kneserNey.containsKey(words[i])) ? kneserNey.get(words[i]) : 0));
        }

        for (Map.Entry<String, Double> entry : kneserNey.entrySet()) {
            if (entry.getValue() == 1) {
                unknownCount += 1;
            }
        }

        int unigramCount = kneserNey.size();

        for (int i = 0; i < len; i++) {
            if (kneserNey.get(words[i]) == 1 &&
                    rand.nextDouble() < UNKNOWN_PROPORTION * unknownCount / unigramCount) {
                words[i] = UNKNOWN;
            }
        }

        kneserNey.clear();
    }
	
	public NGram getDictionary() {
		return ngrams[0];
	}
	
	public String getUNKNOWN() {
		return UNKNOWN;
	}
	
    /**
     *
     * @param str
     * @return how may times string str occurst in text
     */
    public int occurences(String str) {
		int occurence;
		for(int i = NUMBER_OF_NGRAMS-1; i >= 0; i--)
			if((occurence = ngrams[i].occurences(str)) > 0)
				return occurence;
        return 0;
    }

    /**
     * prints all of the ngrams
     */
    public void printNGrams() {
		for(int i = 0; i < NUMBER_OF_NGRAMS; i++)
			ngrams[i].print();
    }

}
