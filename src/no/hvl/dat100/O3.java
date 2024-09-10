package no.hvl.dat100;

import java.math.BigInteger;

public class O3 {
	// non-recursive version will not get stack overflow problems
	static BigInteger factorialByIteration(BigInteger n) 
	{
		if(n.compareTo(BigInteger.ZERO) < 0) 
		{
			throw new IllegalArgumentException("input must be greater or equal to 0");
		}
		
		if(n.compareTo(BigInteger.ONE) <= 0) {
			return BigInteger.ONE;
		}
		
		BigInteger result = n;
				
		while(n.compareTo(BigInteger.ONE) > 0) 
		{
			// decrease by one
			n = n.subtract(BigInteger.ONE);	
			
			// multiply with 1
			result = result.multiply(n);
		
		}
		
		return result; 
	}
	
	// recursive version will get stack overflow problems
	static BigInteger factorial(BigInteger  n) 
	{
		if(n.compareTo(BigInteger.ZERO) < 0) 
		{
			throw new IllegalArgumentException("input must be greater or equal to 0");
		}
		
		if(n.equals(BigInteger.ZERO) || n.equals(BigInteger.ONE)) 
		{
			return BigInteger.ONE;
		}
		
		return n.multiply(factorial(n.subtract(BigInteger.ONE)));
	}

	public static void main(String[] args) {
		try {
			// hent inn heltall og beregn fakultet
			BigInteger num = Dialogs.GenericInputDialog.show(
				null, 
				"Skriv inn heltall (BigInteger supported)", 
				"Input",
				new Dialogs.GenericInputDialog.BigIntegerParser(), 
				true
				);
			
		
			BigInteger fact2 = factorialByIteration(num);					
			System.out.printf("iteration: %d! or factorial(%d) is equal to %s\n", num, num, String.valueOf(fact2));
			
			BigInteger fact1 = factorial(num);
			System.out.printf("recursion %d! or factorial(%d) is equal to %s\n", num, num, String.valueOf(fact1));			
			
			if(!fact1.equals(fact2)) {
				System.out.println("factorials differ");
			}			
		}catch(Exception e) {
			// skriv ut feilmelding
			System.out.printf("Feil: %s\n", e.getMessage());
			
			if(Dialogs.ConfirmationDialog.show(null, "Vil du prøve på nytt?")) {
				main(args);	
			}
		}
	}
}