package nlp.datastructures;

/**
 * A data structure that stores a string and a double value
 * @author Muti Kara
 * */
public class ProbableString implements Comparable<ProbableString>{
	String str;
	double probability;
	
	public ProbableString(String str, double probability){
		this.str = str;
		this.probability = probability;
	}
	
	public double getProbability() {
		return probability;
	}
	
	public String getStr() {
		return str;
	}
	
	public void setStr(String str) {
		this.str = str;
	}
	
	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	@Override
	public int compareTo(ProbableString other) {
		return Double.compare(probability, other.probability);
	}
	
	@Override
	public String toString() {
		return str;
	}
}




