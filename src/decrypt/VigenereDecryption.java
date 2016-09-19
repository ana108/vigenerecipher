package decrypt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VigenereDecryption {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File f = new File("cipher40.txt");
		Scanner sc = null;
		String massiveText = "";
		try {
			sc = new Scanner(f);
			while(sc.hasNext()){
				massiveText += sc.nextLine();
			}
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		VigenereDecryption dc = new VigenereDecryption();
		VigenereKeySize vc = new VigenereKeySize();
		int keySize = vc.publicKeySize(massiveText);
		
		List<List<Character>> columns = dc.frequencyLetters(massiveText, keySize);
		
		System.out.println("Key: "+dc.key(keySize, columns));
		System.out.println(dc.decrypt(massiveText, dc.key(keySize, columns)));
	}
	private String key(int keySize, List<List<Character>> columns){
		String key = ""; // new char[keySize];
		char[] cipherE = new char[keySize];
		for(int i = 0; i < keySize; i++){
			cipherE[i] = checkMostFrequent(frequencyLetter(columns.get(i)));
		}
		int E = (int)'e';
		for(int j = 0; j < keySize; j++){
			if(cipherE[j] == '?'){
				key += '?';
			}
			else if(((int)cipherE[j] - E) < 0){
				char val = (char)((((int)cipherE[j]) - E)+97+26);
				key += Character.toString((val));
			}
			else{
				int val = ((((int)cipherE[j])-E)+97);
				key += Character.toString((char)val);
			}
			
		}
		
		return key.toString();
	}
	public char checkMostFrequent(List<CipherLetter> letters){
		// compare the last two, if the differencec is 4 percent or greater, return e, otherwise return "uncertain"
		/*double difference = letters.get(letters.size()-1).getPercentage() - letters.get(letters.size()-2).getPercentage();
		System.out.println("First largest: " + letters.get(letters.size()-1).getPercentage() +" second largest "+ letters.get(letters.size()-2).getPercentage());
		if(Math.abs(difference) >= 4){
			return String.valueOf(letters.get(letters.size()-1).getWord());
		}
		else{
			return "?";
		}*/
		if(letters.get(letters.size()-1).getPercentage() >= 11){
			return letters.get(letters.size()-1).getWord();
		}
		else {
			return '?';
		}
	}
	
	//returns a key sized list of lists containing the letters in the corresponding column.
	public List<List<Character>> frequencyLetters(String message, int keysize){
		message = message.replace(" ","");
		List<List<Character>> arrayOfArrays = new ArrayList<List<Character>>();
		for(int i = 0; i < keysize; i++){
			List<Character> charactersPerColumn = new ArrayList<Character>();
			arrayOfArrays.add(charactersPerColumn); // leave blank
		}
		char[] letters = message.toCharArray();
		int index = 0;
		for(int j = 0; j < letters.length; j++){
			if(index%keysize == 0){
				index = 0;
			}
			arrayOfArrays.get(index).add(letters[j]);
			index++;
		}
		return arrayOfArrays;
	}
	
	public List<CipherLetter> frequencyLetter(List<Character> list){
		Map<Character, Integer> freq = new HashMap<Character, Integer>();
		
		for(Character c : list){
			if(freq.containsKey(c)){
				freq.put(c, freq.get(c) + 1);
			}
			else{
				freq.put(c, 1);
			}
		} // done.
		
		// sort by frequency and return a list of CipherLetters
		List<CipherLetter> ciphers = new ArrayList<CipherLetter>();
		for(Character s : freq.keySet()){
			double percentage = (double)freq.get(s)/(double)list.size();
			percentage = percentage*100;
			CipherLetter cl = new CipherLetter(s, freq.get(s), percentage);
			ciphers.add(cl);
		}
		Collections.sort(ciphers,new Comparator<CipherLetter>()
				{
		    @Override
		    public int compare(CipherLetter obj1, CipherLetter obj2) {
			       int p1 = obj1.getFrequency();
			       int p2 = obj2.getFrequency();

			       if (p1 > p2) {
			           return 1;
			       } else if (p1 < p2){
			           return -1;
			       } else {
			           return 0;
			       }
			    }
		});
		return ciphers;
	}
	public String decrypt(String message, String key){
		char[] ciphertext = message.toCharArray();
		char[] keyArr = key.toCharArray();
		int[] keyNum = new int[keyArr.length];
		
		for(int i = 0; i < keyArr.length; i++){
			keyNum[i] = ((int)keyArr[i]);
		}
		
		String[] plaintext = new String[ciphertext.length];	
		int keyIndex = 0;
		for(int i = 0; i < ciphertext.length; i++){
			if(ciphertext[i] == ' '){
				plaintext[i] = " ";
				continue;
			}else{
				int temp = ((int)ciphertext[i]) - keyNum[keyIndex%keyNum.length];
				keyIndex++;
				if(temp < 0){
					temp = temp + 26+97;
				}
				else{
					temp = temp + 97;
				}
				plaintext[i] = Character.toString((char)temp);
			}
		}
		String m = "";
		for(String s: plaintext){
			m+=s;
		}
		return m;
	}
}
