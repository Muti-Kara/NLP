package nlp.spellcheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
* StringSimilariyFinder
* @author Muti Kara
* 
* This class finds the weighted difference between two strings.
* As default all of the weights are accepted as 1.
*/
public class StringDiffFinder {
	static int numOfCharacters = 26;
	static int[] ins = new int[numOfCharacters]; // weight of not writing that letter 
	static int[] del = new int[numOfCharacters]; // weight of writing that letter exstra
	static int[][] sub = new int[numOfCharacters][numOfCharacters]; // weight of writing letter "a" as "b" accidental
	String projectDir = "/home/yuio/project/code/nlp/";
	String fileNameSubs = projectDir + "spellcheck/data/substitution.txt";
	String fileNameIns = projectDir + "spellcheck/data/insertion.txt";
	String fileNameDel = projectDir + "spellcheck/data/deletion.txt";
	String str1, str2;
	int[][] dp;
	
	/**
	* Constructor takes two string as parameters to compare.
	* These strings should be shorter than 1000.
	* @param str1
	* @param str2
	 * @throws FileNotFoundException
	 */
	public StringDiffFinder() throws FileNotFoundException{
		Scanner in = new Scanner(new File(fileNameIns));
		for(int i = 0; i < numOfCharacters; i++){
			ins[i] = in.nextInt();
		}
		in.close();
		in = new Scanner(new File(fileNameDel));
		for(int i = 0; i < numOfCharacters; i++){
			del[i] = in.nextInt();
		}
		in.close();
		in = new Scanner(new File(fileNameSubs));
		for(int i = 0; i < numOfCharacters; i++){
			for(int j = 0; j < numOfCharacters; j++){
				sub[i][j] = in.nextInt();
			}
		}
	}
	
	/**
	* 
	* @return difference of string
	 */
	public int difference(String str1, String str2) {
		this.str1 = str1.toLowerCase();
		this.str2 = str2.toLowerCase();
		this.dp = new int[str1.length()][str2.length()];
		for(int i = 0; i < str1.length(); i++)
			for(int j = 0; j < str2.length(); j++)
				dp[i][j] = -1;
		return difference(0, 0);
	}
	
	/**
	* 
	* @param i
	* @param j
	* @return difference of str1[i:], str2[j:]
	 */
	public int difference(int i, int j) {
		if(i == str1.length() && j == str2.length())
			return 0;
		if(i == str1.length() || j == str2.length())
			return 2_000_000;
		if(dp[i][j] != -1)
			return dp[i][j];
		
		dp[i][j] = difference(i+1, j) + Math.min(ins[str1.charAt(i) - 'a'], del[str1.charAt(i) - 'a']);
		dp[i][j] = Math.min(dp[i][j], difference(i, j+1) + Math.min(ins[str2.charAt(j) - 'a'], del[str2.charAt(j) - 'a']));
		if(str1.charAt(i) == str2.charAt(j)){
			dp[i][j] = Math.min(dp[i][j], difference(i+1, j+1));
			dp[i][j] = Math.min(dp[i][j], difference(i, j+1));
			dp[i][j] = Math.min(dp[i][j], difference(i+1, j));
		}else{
			dp[i][j] = Math.min(dp[i][j], difference(i+1, j+1) + Math.min(sub[str1.charAt(i) - 'a'][str2.charAt(j) - 'a'], sub[str2.charAt(j) - 'a'][str1.charAt(i) - 'a']));
			dp[i][j] = Math.min(dp[i][j], difference(i, j+1) + Math.min(sub[str1.charAt(i) - 'a'][str2.charAt(j) - 'a'], sub[str2.charAt(j) - 'a'][str1.charAt(i) - 'a']));
			dp[i][j] = Math.min(dp[i][j], difference(i+1, j) + Math.min(sub[str1.charAt(i) - 'a'][str2.charAt(j) - 'a'], sub[str2.charAt(j) - 'a'][str1.charAt(i) - 'a']));
		}
			
		return dp[i][j];
	}
}
