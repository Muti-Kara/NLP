package nlp.stemming;

/**
* Porters_algorithm
* This class implements a version of this algorithm
* @author Muti Kara
*/
public class PortersAlgorithm {
	char[] word;
	char[] cv;
	int length;
	public int firstVowel = 1_000_000;
	
	public PortersAlgorithm(String word){
		this.word = word.toCharArray();
		this.length = word.length();
		this.cv = new char[length];
		for(int i = 0; i < length; i++){
			cv[i] = (isConsonant(i))? 'c' : 'v';
			firstVowel = (cv[i] == 'v' && firstVowel == 1_000_000)? i : firstVowel;
		}
	}
	
	public String getStemmedString() {
		return String.valueOf(word, 0, length);
	}
	
	public String stemming() {
		int flag = 0;
		// Step 1a
		if(endsWith("sses") || endsWith("ies")){
			length -= 2;
		}
		if(!endsWith("ss") && endsWith("s"))
			length--;
		// Step 1b
		if(endsWith("eed") && findM(3) > 0){
			length--;
		}
		if(hasVowel(2) && endsWith("ed") && !endsWith("eed")){
			length -= 2;
			flag = 1;
		}
		if(hasVowel(3) && endsWith("ing")){
			length -= 3;
			flag = 1;
		}
		if(flag == 1){
			if(endsWith("at") || endsWith("bl") || endsWith("iz")){
				length++;
				word[length-1] = 'e';
				cv[length-1] = 'v';
				flag = 2;
			}
			if(flag == 1 && doubleConsonant() && 
					word[length-1] != 'l' && 
					word[length-1] != 's' && 
					word[length-1] != 'z'){
				length--;
				flag = 2;
			}
			if(flag == 1 && findM(0) == 1 && endsWithCVC()){
				length++;
				word[length-1] = 'e';
				cv[length-1] = 'v';
			}
		}
		// Step 1c
		if (endsWith("y") && hasVowel(1)) {
			word[length-1] = 'i';
		}
		// Step 2
		if(length > 1)
		switch (word[length-2]){
		case 'a':
			if(endsWith("ational") && findM(7) > 0){
				length -= 4;
				word[length-1] = 'e';
				break;
			}
			if(endsWith("tional") && findM(6) > 0){
				length -= 2;
				break;
			}
			break;
		case 'c':
			if(endsWith("enci") && findM(4) > 0){
				word[length-1] = 'e';
				break;
			}
			if(endsWith("anci") && findM(4) > 0){
				word[length-1] = 'e';
				break;
			}
			break;
		case 'e':
			if(endsWith("izer") && findM(4) > 0){
				length--;
				break;
			}
			break;
		case 'l':
			if(endsWith("abli") && findM(4) > 0){
				word[length-1] = 'e';
				break;
			}
			if(endsWith("alli") && findM(4) > 0){
				length -= 2;
				break;
			}
			if(endsWith("entli") && findM(5) > 0){
				length -= 2;
				break;
			}
			if(endsWith("eli") && findM(3) > 0){
				length -= 2;
				break;
			}
			if(endsWith("ousli") && findM(5) > 0){
				length -= 2;
				break;
			}
			break;
		case 'o':
			if(endsWith("ization") && findM(7) > 0){
				length -= 4;
				word[length-1] = 'e';
				break;
			}
			if(endsWith("ation") && findM(5) > 0){
				length -= 2;
				word[length-1] = 'e';
				break;
			}
			if(endsWith("ator") && findM(4) > 0){
				length--;
				word[length-1] = 'e';
				break;
			}
			break;
		case 's':
			if(endsWith("alism") && findM(5) > 0){
				length -= 3;
				break;
			}
			if(endsWith("iveness") && findM(7) > 0){
				length -= 4;
				break;
			}
			if(endsWith("fulness") && findM(7) > 0){
				length -= 4;
				break;
			}
			if(endsWith("ousness") && findM(7) > 0){
				length -= 4;
				break;
			}
			break;
		case 't':
			if(endsWith("aliti") && findM(5) > 0){
				length -= 3;
				break;
			}
			if(endsWith("iviti") && findM(5) > 0){
				length -= 2;
				word[length-1] = 'e';
				break;
			}
			if(endsWith("biliti") && findM(6) > 0){
				length -= 3;
				word[length - 2] = 'l';
				word[length - 1] = 'e';
				break;
			}
			break;
		}
		// Step 3
		if(length>0)
		switch (word[length-1]) {
			case 'e':
				if(endsWith("icate") && findM(5) > 0){
					length -= 3;
					break;
				}
				if(endsWith("ative") && findM(5) > 0){
					length -= 5;
					break;
				}
				if(endsWith("alize") && findM(5) > 0){
					length -= 3;
				}
				break;
			case 'i':
				if(endsWith("iciti") && findM(5) > 0){
					length -= 3;
				}
				break;
			case 'l':
				if(endsWith("ical") && findM(4) > 0){
					length -= 2;
					break;
				}
				if(endsWith("ful") && findM(3) > 0){
					length -= 3;
				}
				break;
			case 's':
				if(endsWith("ness") && findM(4) > 0){
					length -= 4;
				}
				break;
		}
		// Step 4
		if(length>0)
		switch (word[length-1]) {
			case 'e':
				if(endsWith("ance") && findM(4) > 1){
					length -= 4;
					break;
				}
				if(endsWith("ence") && findM(4) > 1){
					length -= 4;
					break;
				}
				if(endsWith("able") && findM(4) > 1){
					length -= 4;
					break;
				}
				if(endsWith("ible") && findM(4) > 1){
					length -= 4;
					break;
				}
				if(endsWith("ate") && findM(3) > 1){
					length -= 3;
					break;
				}
				if(endsWith("ive") && findM(3) > 1){
					length -= 3;
					break;
				}
				if(endsWith("ize") && findM(3) > 1){
					length -= 3;
				}
				break;
			case 't':
				if(endsWith("ement") && findM(4) > 1){
					length -= 5;
					break;
				}
				if(endsWith("ment") && findM(4) > 1){
					length -= 4;
					break;
				}
				if(endsWith("ent") && findM(3) > 1){
					length -= 3;
					break;
				}
				if(endsWith("ant") && findM(3) > 1){
					length -= 3;
				}
				break;
			case 'n':
				if(endsWith("sion") && findM(4) > 1){
					length -= 5;
					break;
				}
				if(endsWith("tion") && findM(4) > 1){
					length -= 4;
				}
				break;
			case 'c':
				if(endsWith("ic") && findM(2) > 1){
					length -= 2;
				}
				break;
			case 'l':
				if(endsWith("al") && findM(2) > 1){
					length -= 2;
				}
				break;
			case 'r':
				if(endsWith("er") && findM(2) > 1){
					length -= 2;
				}
				break;
			case 'u':
				if(endsWith("ou") && findM(2) > 1){
					length -= 2;
				}
				break;
			case 'm':
				if(endsWith("ism") && findM(3) > 1){
					length -= 3;
				}
				break;
			case 'i':
				if(endsWith("iti") && findM(3) > 1){
					length -= 3;
				}
				break;
			case 's':
				if(endsWith("ous") && findM(3) > 1){
					length -= 3;
				}
				break;
		}
		//Step 5a
		if(endsWith("e") && findM(1) > 1){
			length--;
		}else if (endsWith("E") && findM(1) == 1 && !endsWithCVC()){
			length--;
		}
		//Step 5b
		if(doubleConsonant() && findM(2) > 1 && word[length-1] == 'l')
			length--;
		return getStemmedString();
	}
	
	/**
	* 
	* @param end
	* @return does the word ends with end
	 */
	public boolean endsWith(String end) {
		int endLength = end.length();
		if(endLength > length)
			return false;
		for(int i = length-1, j = endLength-1; j >= 0; i--, j--){
			if(word[i] != end.charAt(j))
				return false;
		}
		return true;
	}
	
	/**
	* 
	* @return does the word ends with double consonant
	 */
	public boolean doubleConsonant() {
		if(length<2)
			return false;
		return cv[length-1] == 'c' && word[length-1] == word[length-2];
	}
	
	/**
	* 
	* @return does the word ends with consonant vowel (consonant not 'w, x, y')
	 */
	public boolean endsWithCVC() {
		if(length < 3)
			return false;
		return cv[length-1] == 'c' && cv[length-2] == 'v' && cv[length-3] == 'c' 
		&& word[length-1] != 'w' && word[length-1] != 'x' && word[length-1] != 'y';
	}
	
	/**
	* 
	* @param suffixLength
	* @return does the word has vowel sound
	 */
	public boolean hasVowel(int suffixLength) {
		return firstVowel < length - suffixLength;
	}
	
	/**
	* [vvv...] = V
	* [ccc...] = C
	* @param suffixLength
	* @return number of VC blocks
	 */
	public int findM(int suffixLength) {
		int m = 0;
		for(int i = 0; i < length-1-suffixLength; i++){
			if(cv[i] == 'v' && cv[i+1] == 'c')
				m++;
		}
		return m;
	}
	
	/**
	* 
	* @param idx
	* @return is word[idx] consonant
	 */
	public boolean isConsonant(int idx) {
		if( word[idx] == 'a' || 
			word[idx] == 'e' ||
			word[idx] == 'i' ||
			word[idx] == 'o' ||
			word[idx] == 'u' )
			return false;
		if( word[idx] == 'y' && idx != 0 && isConsonant(idx-1))
			return false;
		return true;
	}
}
