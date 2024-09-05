package no.hvl.dat100;


/*
 * this class is allready defined in no.hvl.dat100 package
 * 
class GenericInputDialog{
	private interface Parser<T>{ T parse(String input); }
	public static class ShortParser implements Parser<Short>{ @Override public Short parse(String s) { return Short.parseShort(s); } }
	public static class ByteParser implements Parser<Byte>{ @Override public Byte parse(String s) { return Byte.parseByte(s); } }
	public static class IntParser implements Parser<Integer>{ @Override public Integer parse(String s) { return Integer.parseInt(s); } }
	public static class LongParser implements Parser<Long>{ @Override public Long parse(String s) { return Long.parseLong(s); } }
	public static class FloatParser implements Parser<Float>{ @Override public Float parse(String s) { return Float.parseFloat(s); } }
	public static class DoubleParser implements Parser<Double>{ @Override public Double parse(String s) { return Double.parseDouble(s); } }
	public static class BooleanParser implements Parser<Boolean>{ @Override public Boolean parse(String s) { return Boolean.parseBoolean(s); } }
	public static class CharParser implements Parser<Character>{ @Override public Character parse(String s) { return s.charAt(0); } }
	public static class StringParser implements Parser<String>{ @Override public String parse(String s) { return s; } }

	public static <T> T read(Object parentComponent, String message, String title, Parser<T> parser) {
		return read(parentComponent, message, title, parser, false);
	}
	public static <T> T read(Object parentComponent, String message, String title, Parser<T> parser, boolean retry) {
		try {
			String s = javax.swing.JOptionPane.showInputDialog((java.awt.Component) parentComponent, message, title, javax.swing.JOptionPane.QUESTION_MESSAGE);
			return parser.parse(s);
		}catch(Exception e) {
			if(retry) {
				return read(parentComponent, message, title, parser, retry);
			}else {
				throw(e);
			}
		}
	}
};
*/


public class O2 {
	// validate score
	static boolean isValidScore(int score) {
		return (score >= 0) && (score <= 100);
	}
	
	/*
	 * map a score to a grade. We could have use a char, but a String
	 * is chosen in case we want to add + or - or change grade-"enum"
	 * later on.
	 * 
	 * else-statements are redundant since each if-true returns a value
	 */
	static String scoreToGradeString(int score){
		if(!isValidScore(score)) {
			throw new IllegalArgumentException("Poeng må være i [0..100]");
		}

		if(score <= 39) { 
			return "F"; 
		}		
		if(score <= 49) {
			return "E"; 
		}		
		if(score <= 59) { 
			return "D"; 
		}		
		if(score <= 79) { 
			return "C"; 
		}		
		if(score <= 89) { 
			return "B"; 
		}		
		return "A";
	}
	
	/*
	 * - n angir hvor mange ganger en vil mappe en poengsum til en karakter
	 * - retryIfInvalidGrade angir om en skal lese inn på nytt til en får
	 * en gyldig karakter (altså at poengsum er gyldig) 
	 */
	public static void run(int n, boolean retryIfInvalidGrade) {
		
		for(int i=0; i<n; i++) {
			try {
				// hent inn poengsum (score) og kalkulert karakter
				int score = GenericInputDialog.read(
					null, 
					"Skriv inn poengsum", 
					"Input",
					new GenericInputDialog.IntParser(), 
					true
					);
				String grade = scoreToGradeString(score);
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
	public static void bruteForceTest() {	
		String prevGrade = null;
		java.util.ArrayList<Integer> scores = new java.util.ArrayList<Integer>(); 
		
		for(int score = -3; score < 104; score++) {
			try {
				String grade = scoreToGradeString(score);
				
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
		if(javax.swing.JOptionPane.showConfirmDialog(null, "Vil du kjøre tilleggstest?") == javax.swing.JOptionPane.YES_OPTION) {
			bruteForceTest();
		}
	}
}
