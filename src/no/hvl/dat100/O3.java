package no.hvl.dat100;

public class O3 {
	static int factorial(int n) {
		if(n < 0) {
			throw new IllegalArgumentException("input must be greater or equal to 0");
		}
		
		if(n <= 1) {
			return 1;
		}
		
		return n * factorial(n - 1);
	}

	public static void main(String[] args) {
		try {
			// hent inn heltall og beregn fakultet
			int num = GenericInputDialog.read(
				null, 
				"Skriv inn heltall", 
				"Input",
				new GenericInputDialog.IntParser(), 
				true
				);
			int fact = factorial(num);
			System.out.printf("!%d or factorial(%d) is equal to %d\n", num, num, fact);
		}catch(Exception e) {
			// skriv ut feilmelding
			System.out.printf("Feil: %s\n", e.getMessage());
			
			if(javax.swing.JOptionPane.showConfirmDialog(null, "Vil du prøve på nytt?") == javax.swing.JOptionPane.YES_OPTION) {
				main(args);	
			}
		}
	}

}
