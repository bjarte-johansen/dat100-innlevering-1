package no.hvl.dat100;

import java.math.BigInteger;

import java.text.NumberFormat;
import java.util.Locale;

class Dialogs{
	public static class GenericInputDialog{
		private interface Parser<T>{ T parse(String input); }
		public static class ShortParser implements Parser<Short>{ @Override public Short parse(String s) { return Short.parseShort(s); } };
		public static class ByteParser implements Parser<Byte>{ @Override public Byte parse(String s) { return Byte.parseByte(s); } };
		public static class IntParser implements Parser<Integer>{ @Override public Integer parse(String s) { return Integer.parseInt(s); } };
		public static class LongParser implements Parser<Long>{ @Override public Long parse(String s) { return Long.parseLong(s); } };
		public static class FloatParser implements Parser<Float>{ @Override public Float parse(String s) { return Float.parseFloat(s); } };
		public static class DoubleParser implements Parser<Double>{ @Override public Double parse(String s) { return Double.parseDouble(s); } };
		public static class BooleanParser implements Parser<Boolean>{ @Override public Boolean parse(String s) { return Boolean.parseBoolean(s); } };
		public static class CharParser implements Parser<Character>{ @Override public Character parse(String s) { return s.charAt(0); } };
		public static class StringParser implements Parser<String>{ @Override public String parse(String s) { return s; } };
		public static class BigIntegerParser implements Parser<BigInteger>{ @Override public BigInteger parse(String s) { return new BigInteger(s); } };
	
		public static <T> T show(java.awt.Component parentComponent, String message, String title, Parser<T> parser) {
			return show(parentComponent, message, title, parser, false);
		}
		public static <T> T show(java.awt.Component parentComponent, String message, String title, Parser<T> parser, boolean retry) {
			try {
				String s = javax.swing.JOptionPane.showInputDialog(parentComponent, message, title, javax.swing.JOptionPane.QUESTION_MESSAGE);
				return parser.parse(s);
			}catch(Exception e) {
				if(retry) {
					return show(parentComponent, message, title, parser, retry);
				}else {
					throw(e);
				}
			}
		}
	};

	public static class ConfirmationDialog{
		/*
		public static boolean show(String message) {
			return show(null, message);
		}
		*/
		public static boolean show(java.awt.Component parentComponent, String message) {
			int option = javax.swing.JOptionPane.showConfirmDialog(parentComponent, message);
			return option == javax.swing.JOptionPane.YES_OPTION;
		}
	};
};


/*
 * vi har valgt å skrive ut litt tillegginformasjon forbi oppgavetekst
 * og blat annet lekt litt med numberFormat men 
 */

public class O1 {
	// format norwegian currency
	static String formatCurrency(double amt) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("no", "NO"));
        return formatter.format(amt); 
	}
	
	// compute tax percentage
	// - throws if input is invalid
	static double computeTaxPercentage(double income) {
		if(income < 0) {
			throw new IllegalArgumentException("ugyldig inntekt");
		}		
		if(income < 208050) {
			return 0.0;
		}
		if(income < 292850) {
			return 1.7;
		}
		if(income < 670000) {
			return 4.0;			
		}
		if(income < 937900) {
			return 13.6;
		}
		if(income < 1350000) {
			return 16.6;
		}
		return 17.6;
	};
	
	
	/*
	 * main function for tax-class
	 * - get income before tax as a double and show tax-percentage
	 * as output 
	 */
	static void run() {
		try {
			double incomeBeforeTax = Dialogs.GenericInputDialog.show(
				null, 
				"Skriv inn inntekt før skatt", 
				"Input",
				new Dialogs.GenericInputDialog.DoubleParser(), 
				true
				);
			double taxPercentage = computeTaxPercentage(incomeBeforeTax);
			double taxToPay = incomeBeforeTax * (taxPercentage / 100.0);
			double incomeAfterTax = incomeBeforeTax - taxToPay;
			
			System.out.printf("Du tjente ca %s kroner før skatt\n", formatCurrency(incomeBeforeTax));
			System.out.printf("Din trinnskatt er ca %s%%\n", taxPercentage, String.valueOf(taxPercentage));
			System.out.printf("Du skal betale ca %s kroner i trinnskatt\n", formatCurrency(taxToPay));
			System.out.printf("Inntekt etter trinnskatt er %s kroner\n", formatCurrency(incomeAfterTax));
		} catch(Exception e) {
			System.out.println("404 - du har funnet en feil vi ikke fant");
			System.out.println("Kjør program på nytt med gyldig input");
			System.out.printf("Detaljer: \"%s\"\n", e.toString());
		}
	}
	
	public static void main(String[] args) {
		run();
	}
}