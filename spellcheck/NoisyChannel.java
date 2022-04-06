package nlp.spellcheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

import nlp.datastructures.ProbableString;

/**
* NoisyChannel
* A simple noisy channel for spellcheking
* 
* @author Muti Kara
*/
public class NoisyChannel {
	// This boolean is a flag for determine whether our word exist or not.
	boolean isRealWord = false;
	
	HashSet<String> dictionary;
	StringDiffFinder diffFinder;
	
	String projectDir = "/home/yuio/project/code/";
	String fileNameDict = projectDir + "/nlp/spellcheck/data/dictionary.txt";;
	
	/**
	* Reads dictionary.
	* Initiates String Difference Finder
	* @throws FileNotFoundException
	 */
	public NoisyChannel() throws FileNotFoundException{
		diffFinder = new StringDiffFinder();
		dictionary = new HashSet<>();
		Scanner in = new Scanner(new File(fileNameDict));
		while(in.hasNext()){
			dictionary.add(in.next());
		}
	}
	
	/**
	* Takes an input string str.
	* Generates candidates by looking their probabilities.
	* @param str
	* @return
	 */
	public ArrayList<ProbableString> candidates(String str){
		ArrayList<ProbableString> candidates = new ArrayList<>();
		for(String word : dictionary){
			if(word.equals(str)){
				isRealWord = true;
				candidates.add(new ProbableString(word, 20 * diffFinder.difference(word, str)));
			}else{
				candidates.add(new ProbableString(word, diffFinder.difference(word, str)));
			}
		}
		Collections.sort(candidates);
		return candidates;
	}
	
}

