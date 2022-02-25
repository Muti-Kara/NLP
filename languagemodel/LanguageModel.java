package nlp.languagemodel;

import nlp.stemming.PortersAlgorithm;
import nlp.datastructures.ngrams.*;
import nlp.ngram.*;

/**
 * LanguageModel
 * Provides a language model
 * @author Muti Kara
 */
public class LanguageModel {
    double QUAD_WEIGHT = .7;
    double TRI_WEIGHT = .4;
    double BI_WEIGHT = .25;
    double UNI_WEIGHT = .15;
    double DISCOUNT = .5;
	
    String[] words;
    NGramManager ng;
	NGram dictionary;
    int n = 4;

	/**
	* 
	* @param ng n-gram manager
	* @param n do we use only biagrams or trigrams
	 */
    public LanguageModel(NGramManager ng, int n) {
        this.ng = ng;
        this.n = n;
		this.dictionary = ng.getDictionary();

        switch (n) {
            case 1:
                UNI_WEIGHT = 1.2;
                break;
            case 2:
                UNI_WEIGHT = .2;
                BI_WEIGHT = .9;
                break;
            case 3:
                UNI_WEIGHT = .2;
                BI_WEIGHT = .5;
                TRI_WEIGHT = .8;
                break;
        }
    }
	
	/**
	* Default setting is using quadgrams
	* @param ng
	 */
    public LanguageModel(NGramManager ng) {
        this.ng = ng;
		this.dictionary = ng.getDictionary();
    }
	
    /**
     * @param lastIndex
     * @return probability of words[lastIndex] coming after previous few words
     */
    private double languageModel(int lastIndex) {
        String gotSoFar = "";
        double uniprobability = (ng.kneserNey.containsKey(words[lastIndex - 1]))? ng.kneserNey.get(words[lastIndex - 1]) : 0;
        if (n == 1 || lastIndex == 1)
            return uniprobability * UNI_WEIGHT;

        gotSoFar = words[lastIndex - 2];
        double biprobability = Math.max(ng.occurences(gotSoFar + " " + words[lastIndex - 1]) - DISCOUNT, 0);
        biprobability /= ng.occurences(gotSoFar);
        if (n == 2 || lastIndex == 2)
            return biprobability * BI_WEIGHT + uniprobability * UNI_WEIGHT;

        gotSoFar = words[lastIndex - 3] + " " + gotSoFar;
        double triprobability = Math.max(ng.occurences(gotSoFar + " " + words[lastIndex - 1]) - DISCOUNT, 0);
        triprobability /= ng.occurences(gotSoFar);
        if (n == 3 || lastIndex == 3)
            return triprobability * TRI_WEIGHT + biprobability * BI_WEIGHT + uniprobability * UNI_WEIGHT;

        gotSoFar = words[lastIndex - 4] + " " + gotSoFar;
        double quadprobability = Math.max(ng.occurences(gotSoFar + " " + words[lastIndex - 1]) - DISCOUNT, 0);
        quadprobability /= ng.occurences(gotSoFar);
        return quadprobability * QUAD_WEIGHT + triprobability * TRI_WEIGHT +
                biprobability * BI_WEIGHT + uniprobability * UNI_WEIGHT;
    }

    /**
     *
     * @param str
     * @param partial = true
     * @return it looks probability of last part of string.
     */
    public double probability(String str, boolean partial) {
        str = str.replaceAll("[^A-Za-z ]", "");
        str = str.toLowerCase();
        words = str.split(" ");
        for (int i = 0; i < words.length; i++) {
            PortersAlgorithm pa = new PortersAlgorithm(words[i]);
            words[i] = pa.stemming();
        }

        for (int i = 0; i < words.length; i++) {
            if (!dictionary.getNgrams().containsKey(words[i]))
                words[i] = ng.UNKNOWN;
        }

        if (partial)
            return languageModel(words.length);

        double prob = 0;
        for (int i = words.length; i > 3; i--) {
            prob += Math.log(languageModel(i));
        }
        return Math.exp(prob);
    }

    /**
     *
     * @param str
     * @return it looks probability of entire string
     */
    public double probability(String str) {
        return probability(str, false);
    }

}
