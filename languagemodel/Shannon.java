package nlp.languagemodel;

import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import nlp.ngram.*;
import nlp.datastructures.ngrams.*;
import nlp.datastructures.*;

/**
* Shannon
* A simple shannon game based on languagemodel
* @author Muti Kara
*/
public class Shannon {
	double FIRST = .4;
	double SECOND = .7;
	double THIRD = .9;
	
	LanguageModel lm;
	NGramManager ng;
	NGram dictionary;
	Random rand = new Random();
	
	/**
	* takes a language model
	* @param lm
	 */
	public Shannon(LanguageModel lm) {
		this.lm = lm;
		this.ng = lm.ng;
		this.dictionary = lm.dictionary;
	}
	
	/**
	* 
	* @param beginning
	* @return next possible word
	 */
	public String nextWord(String beginning) {
		ArrayList<ProbableString> arr = new ArrayList<>();
		
		for(Map.Entry<String, Integer> entry : dictionary.getNgrams().entrySet()){
			double prob = lm.probability(beginning + " " + entry.getKey(), true);
			if(Double.isNaN(prob))
				continue;
			arr.add(new ProbableString(entry.getKey(), prob));
		}
		
		if(arr.size() == 0)
			return ng.UNKNOWN;
		
		Collections.sort(arr, Collections.reverseOrder());
		
		if(arr.get(0).getStr().equals(ng.UNKNOWN)){
			FIRST = -1;
		}
		if(arr.get(1).getStr().equals(ng.UNKNOWN)){
			SECOND = -1;
		}
		if(arr.get(2).getStr().equals(ng.UNKNOWN)){
			THIRD = -1;
		}
		
		double select = rand.nextDouble();
		if(select < FIRST)
			return arr.get(0).getStr();
		if(select < SECOND)
			return arr.get(1).getStr();
		if(select < THIRD)
			return arr.get(2).getStr();
		return arr.get(3).getStr();
	}
	
	/**
	* takes beginning string and how man word will it play
	* @param beginning
	* @param word
	 */
	public void shannon(String beginning, int word) {
		while(word-->0){
			beginning += " " + nextWord(beginning);
		}
		System.out.println(beginning);
	}
}
