package decrypt;

public class CipherLetter { // implements Comparator
	private Character word;
	private int frequency;

	CipherLetter(Character w, int f, double p) {
		word = w;
		frequency = f;
		percentage = p;
	}

	public Character getWord() {
		return word;
	}

	public void setWord(Character word) {
		this.word = word;
	}

	public int getFrequency() {
		return frequency;
	}

	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	private double percentage;

	/*
	 * public int compare(Object obj1, Object obj2) { Integer p1 =
	 * ((CipherLetter) obj1).frequency; Integer p2 = ((CipherLetter)
	 * obj2).frequency;
	 * 
	 * if (p1 > p2) { return 1; } else if (p1 < p2){ return -1; } else { return
	 * 0; } }
	 */
}
