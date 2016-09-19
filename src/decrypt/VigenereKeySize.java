package decrypt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

public class VigenereKeySize {
	
    private Map<String, Integer> wordOccurances(String message){
    	String[] words = message.trim().split(" ");
    	Map<String, Integer> counted = new HashMap<String, Integer>();
    	// keep track of highest frequency of repeating words?
    	// get a list of all the places where "word" occurs. use indexOf and start value.
    	for(int i = 0; i < words.length; i++){
    		if(counted.containsKey(words[i].trim())){
    			counted.put(words[i].trim(), counted.get(words[i].trim()) + 1);
    		}
    		else{
    			counted.put(words[i].trim(), 1);
    		}
    	}	
    	return counted;
    }
    // returns the two highest words
    private List<String> mostFrequentWord(String message){
    	Map<String, Integer> rankedWords = wordOccurances(message);
		int maxWord = 0;
		String highestWord = "";
		
		int secondMaxWord = 0;
		String secondHighestWord = "";
		for(String word : rankedWords.keySet()){
				if(word.length() > 2){
				if(rankedWords.get(word) > maxWord){
					maxWord = rankedWords.get(word);
					highestWord = word;
				}
				if(rankedWords.get(word) < maxWord && rankedWords.get(word) > secondMaxWord){
					secondMaxWord = rankedWords.get(word);
					secondHighestWord = word;
				}
			}
		}
		List<String> frequentWords = new ArrayList<String>();
		frequentWords.add(highestWord);
		frequentWords.add(secondHighestWord);
		return frequentWords;
    }
    private List<Integer> differences(Stack<Integer> locations){
    	List<Integer> differences = new ArrayList<Integer>();
    	int previous = locations.pop();
    	int current;
    	while(!locations.isEmpty()){
    		current = locations.pop();
    		differences.add(Math.abs(current - previous));
    		previous = current;
    	}
    	return differences;
    }
    /* Gets the locations of the word passed */
    private Stack<Integer> getLocationOfWord(String word, String message){
    	Stack<Integer> locations = new Stack<Integer>();
    	String cleanedMessage = message.replace(" ", "");	
    
    	int start = cleanedMessage.indexOf(word);
    	locations.push(start);
    	while(locations.peek() != -1){
    		locations.push(cleanedMessage.indexOf(word, locations.peek()+1));
    	}
    	locations.pop();
    	return locations;
    }
    /*private int keySize(List<Integer> locationSizes){
    	if(locationSizes.get(0) < 4){
    		if(locationSizes.size() > 1){
    			return locationSizes.get(0);
    		}
    	}
    	return locationSizes.get(0);
    }*/
    public int publicKeySize(String message){
    	// Big Edge Case: If common factors is empty, try the next most frequent word
		List<String> highestWord = mostFrequentWord(message);
		List<Integer> occurs = differences(getLocationOfWord(highestWord.get(0), message));
		Collections.sort(occurs);
		//System.out.println(occurs);
		PrimeNumbers pm = new PrimeNumbers();
		Set<Integer> locFactors = new HashSet<Integer>(pm.factors(occurs.get(0))); //check for existance
		Set<Integer> temp = new HashSet<Integer>();
		/*
		 * This method finds the factors that are factors of all the differences of location.
		 * For example, say abc is found in message in locations 3 27 and 63.
		 * The differences between all of those locations are 24 and 36.
		 * The factors of 24 are: 2,12,3,4, 24, 6 
		 * The factors of 36 are: 2, 3, 4,6,9,12,18,36
		 * The return values will be the prime factors that are in both, so
		 * 2,3,4,6,12 - This suggests that 12 is a valid key size.
		 * */
		/*
		 * How This section works: 
		 * Each list of prime numbers is a set.
		 * We take the smallest set, 
		 * and then check it against the next biggest set.
		 * If biggest set contains a number in the smaller set,
		 * the number gets put into a third, separate set.
		 * Once done checking the bigger set, we replace the smaller set with the third set.
		 * This smaller set is now checked against the next biggest set, and repeat until all sets are searched.*/
		for(int i = 1; i < occurs.size(); i++){
			List<Integer> secondRound = pm.factors(occurs.get(i));
			for(Integer s : locFactors){			
				if(secondRound.contains(s)){
					temp.add(s);
				}
			}
			locFactors = temp;
		}
		//System.out.println("Mutual Factors: " + locFactors);
		Set<Integer> locFactorsTwo = null;
		/*
		 * Generally speaking, the above method should widdle down the list of 
		 * mutual prime factors to 2 or less.
		 * If there are more than 2 in the list, it needs further confirmation 
		 * that it is not an error.
		 * In this case, we repeat the above process for the second most common word.
		 * In this case, we don't test that the two match; we only return the highest number in the list.
		 * 
		 * *Note* there reason I didn't just use the second most common word, is because in some cases
		 * the second most common word is much more rare, resulting in bigger differences in location.
		 * Bigger numbers result in more factors, which increases the list rather than decreasing it.
		 * We only want the second word to be used, only when the first word has already been tested.
		 * if the list of mutual factors is 0, then something went wrong and we try again with a different word.
		 * */
		if(locFactors.size() > 2 || locFactors.size() == 0){
			List<Integer> occursTwo = differences(getLocationOfWord(highestWord.get(1), message));
			Collections.sort(occursTwo);
			//System.out.println(occursTwo);
			locFactorsTwo = new TreeSet<Integer>(pm.factors(occursTwo.get(0))); //check for existance
			Set<Integer> tempTwo = new TreeSet<Integer>();
			
			for(int i = 1; i < occursTwo.size(); i++){
				List<Integer> secondRound = pm.factors(occursTwo.get(i));
				for(Integer s : locFactorsTwo){			
					if(secondRound.contains(s)){
						tempTwo.add(s);
					}
				}
				locFactorsTwo = tempTwo;
			}
		}
		
		if(locFactorsTwo != null){
			return (int) locFactorsTwo.toArray()[locFactorsTwo.size()-1];
		}
		else{
			return (int) locFactors.toArray()[locFactors.size()-1];
		}
    }
}