package no.hvl.dat100;

public class O2 {
	// validate score
	static boolean isValidScore(int score) {
		return (score >= 0) && (score <= 100);
	}
	
	// map score to grade 
	// - throws if input is invalid	
	static char scoreToGradeString(int score){
		if(!isValidScore(score)) {
			throw new IllegalArgumentException("Poeng må være i [0..100]");
		}

		if(score <= 39) { 
			return 'F'; 
		}		
		if(score <= 49) {
			return 'E'; 
		}		
		if(score <= 59) { 
			return 'D'; 
		}		
		if(score <= 79) { 
			return 'C'; 
		}		
		if(score <= 89) { 
			return 'B'; 
		}		
		return 'A';
	}
	
	/*
	 * - n angir hvor mange ganger en vil mappe en poengsum til en karakter
	 * - retryIfInvalidGrade angir om en skal lese inn på nytt til en får
	 * en gyldig karakter (altså at poengsum er gyldig) 
	 */
	static void run(int n, boolean retryIfInvalidGrade) {
		for(int i=0; i<n; i++) {
			try {
				// hent inn poengsum (score) og kalkulert karakter
				int score = Dialogs.GenericInputDialog.show(
					null, 
					"Skriv inn poengsum", 
					"Input",
					new Dialogs.GenericInputDialog.IntParser(), 
					true
					);
				char grade = scoreToGradeString(score);
				System.out.printf("Poengsum: %d, Karakter: %s\n", score, grade);
			}catch(Exception e) {
				// skriv ut feilmelding
				System.out.printf("Feil: %s, (poengsum var %d)\n", e.getMessage(), i);
				
				// ... og prøv på ny hvis ugyldig karakter
				if(retryIfInvalidGrade)
					run(1, retryIfInvalidGrade);				
			}
		}
	}

	/*
	 * this method tries all scores from -3 to +105 to brute-force 
	 * test our scoreToGradeString algorithm. Every grade will be printed
	 * with all accompanying scores that can be mapped to it.
	 *
	 **/
	static void dumpGrades(java.util.ArrayList<Integer> scores, String grade) {
		if(!scores.isEmpty()) {
			System.out.printf("Grade: \"%s\"\n", grade);						
			System.out.println(scores.toString());						
			scores.clear();
		}
	}
	static void bruteForceTest() {	
		String prevGrade = null;
		java.util.ArrayList<Integer> scores = new java.util.ArrayList<Integer>(); 
		
		for(int score = -3; score < 104; score++) {
			try {
				String grade = String.valueOf(scoreToGradeString(score));
				
				// if new grade encountered, then print all grades that were collected
				// for previous grade, if any, and set prev grade to new grade
				if(!grade.equals(prevGrade)) {
					dumpGrades(scores, prevGrade);
					prevGrade = grade;					
				}
				
				scores.add(score);
			}catch(Exception e) {	
				dumpGrades(scores, prevGrade);		
				System.out.printf("Feil: %s, (poengsum var %d)\n", e.getMessage(), score);
			}
		}
		dumpGrades(scores, prevGrade);	
		System.out.println();
	}

	public static void main(String[] args) {
		run(10, true);
		
		// optional test to print scores (0+(n<0)) .. (100+(n>0)) grades 
		if(Dialogs.ConfirmationDialog.show(null, "Vil du kjøre tilleggstest?")) {
			bruteForceTest();
		}
	}
}
