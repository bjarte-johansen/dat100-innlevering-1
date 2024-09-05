# dat100-innlevering-1

Ps: Innlevering benytter seg av klassen GenericInputDialog som er definert i O1.java og er implementert med generics. 

Metoden read(..., retry) sitt retry parameter angir om dialogboksen skal vises på ny dersom ikke parser-instance gitt
i Parser<T> parser parameteret klarte å parse (tolke) tekst til verdi. 

Dersom retry er satt til true så vil GenericInputDialog
1:
- be om input og forsøke å parse det til generisk return type
- hvis exception, repetere steg 1
- gå til steg 2
2:
- returnere resultat

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
		public static boolean show(String message) {
			return show(null, message);
		}
		public static boolean show(java.awt.Component parentComponent, String message) {
			int option = javax.swing.JOptionPane.showConfirmDialog(parentComponent, message);
			return option == javax.swing.JOptionPane.YES_OPTION;
		}
	};
};
