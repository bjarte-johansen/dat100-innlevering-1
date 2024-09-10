/*
 * this file will not be updated as a newer version has been written. Due to changes in
 * parameters / function names we have chosen to just use our old Generic Dialogs class
 * 
 * dont copy-paste this code or file to other projects
 */

package no.hvl.dat100;

import java.math.BigInteger;

public class Dialogs{
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
	
		// new interface for show and parse
		public static <T> T parse(String stringToParse, Parser<T> parser) {
			return parser.parse(stringToParse);			
		}
		/*
		public static <T> T parse(String stringToParse, Class<T> parser_class_ref) {
			T parser = parser_class_ref.getDeclaredConstructor().newInstance();
			return parser.parse(stringToParse);			
		}
		*/		
		public static String show(java.awt.Component parentComponent, String message, String title) {
			return javax.swing.JOptionPane.showInputDialog(parentComponent, message, title, javax.swing.JOptionPane.QUESTION_MESSAGE);
		}			
		
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
	}

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
	}
}