package decrypt;

import java.util.ArrayList;
import java.util.List;

public class PrimeNumbers {
	public List<Integer> factors(int n){
		List<Integer> factors = new ArrayList<Integer>();
		
		int j = n;//change here, removed -1
		if(n%2 == 0)
			factors.add(2);
		
		while(j > 2){
			if(n%j == 0){
				factors.add(j);
			}
			j--;
		}
		return factors;
	}
}
