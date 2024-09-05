package no.hvl.dat100;

import java.math.BigInteger;

public class O3 {
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
			
			BigInteger fact = factorial(num);
			System.out.printf("%d! or factorial(%d) is equal to %s\n", num, num, String.valueOf(fact));
		}catch(Exception e) {
			// skriv ut feilmelding
			System.out.printf("Feil: %s\n", e.getMessage());
			
			if(Dialogs.ConfirmationDialog.show(null, "Vil du prøve på nytt?")) {
				main(args);	
			}
		}
	}

}
