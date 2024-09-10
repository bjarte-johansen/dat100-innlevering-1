package no.hvl.dat100;

import java.text.NumberFormat;
import java.util.Locale;

/*
 * ps: trinnskatt.pdf sier inntekt 1 350 100.00 NOK gir 121 388.20 NOK i skatt å betale. 
 * Dette går bare an å få til dersom en justerer ned skattegrense til [tall * 100 + 0]
 * ELLER, det eneste som kan være korrekt, å justere inntekt opp mot, men ikke til, neste krone. 
 * Jeg har valgt å legge til 99 øre på hver inntekt da en epost fra lærer sa at "Merk at for 
 * skatt blir desimalane kutta til slutt". Det gikk IKKE klart frem hva dette betydde, men 
 * siden eneste måte vi kan få tall til å gå opp ved bruk av grenseverdier fra skatteetaten
 * er å legge til verdi i desimalkolonnen for inntekt så gjør vi det !! 
 * 
 *   Vi godtar altså IKKE å tilpasse algoritmen for å få det som "fremstår" som korrekt 
 *   fasitsvar. Vi kan vite at vår fremgangsmåte er KORREKT ved å tenke slik som dette siden 
 *   vi godtar at skatten som skal betales må være korrekt oppgitt i trinnskat, selv om inntekten
 *   kan ha fått desimalene rundet ned:
 *   
 *   	i = 1 350 100.00
 *   	g = 1 350 001.00
 *   	p = 17.6
 *   	t = (i - g) * p = 99 * 17.6% = 17.424 = ca 17.40
 *   
 *   Hvis vi legger til en del ører (vi har brukt 99 øre) så får vi
 *   	i = 1 350 100.99
 *   	g = 1 350 001.00
 *   	p = 17.6
 *   	t = (i - g) * p = 99.99 * 17.6% = 17.59824 = ca 17.60
 *
 * PS: vi kunne etter hver skattetrinnutrening ovenfra reknet ut skatt for sum - 
 * trinn, satt resterende inntekt til å være trinnverdi og repetert
 * til vi nådde skattetrinn med 0% skatt, eller 0 income, men vi valgte 
 * å pusse på denne metoden selv om den innebærer en del mer kode.
 * 
 * Forvirring fra oppgavetekst som flere har påpekt gjorde at en valgte å 
 * dokumentere hvorfor det er TILSYNELATENDE avvik mellom fasit og virkelighet
 * og hvorfor man IKKE kan tilpasse grensene sine for å få riktige svar. 
 * Skatt er ikke noe man spøkre med!! 
 * 
 * av denne grunnen er algoritmen litt lenger enn de 5-9 linjene den 
 * kunne vært om vi godtok feil mot fasit, men jeg brukte tiden til 
 * lett banning over avvik og tolke oppgaven flere ganger for deretter
 * å la det gå opp for meg: 
 * 
 * DETTE ER EN FINURLIG LUREOPPGAVE FRA HVL !!! 
 */

class TaxCalculator{
	// not declared as final since they might be updated from a file
	static double[] STEP_INCOMES = new double[] {0.0, 208051.0, 292851.0, 670001.0, 937901.0, 1350001.0};
	static double[] STEP_PERCENTAGES = new double[] {0.0, 1.7, 4.0, 13.6, 16.6, 17.6};

	// optional debug flag to enable debug output
	final static boolean DEBUG = false; 
	
	
	// return false is we are in an invalid state and should not continue
	// - note that the method does not throw itself
	static boolean isValidInternalState() 
	{
		return (STEP_INCOMES.length > 0) && (STEP_INCOMES.length == STEP_PERCENTAGES.length);
	}
	
	// compute tax amount for a certain step
	protected static double computeStepTaxAmount(double income, double stepMinIncome, double stepMaxIncome, double stepTaxPercentage)
	{		
		// compute amount of tax to pay for step
		double stepTaxAmount = 0.0; 
		
		// amount of income in this step			
		double stepIncomeAmount;
		
		// check if we are above step minimum income
		// cause the man needs to get payed
		if(income > stepMinIncome) 
		{
			// compute amount of income for step			
			stepIncomeAmount = Math.min(stepMaxIncome, income) - stepMinIncome;
			
			// compute tax amount
			stepTaxAmount = stepIncomeAmount * (stepTaxPercentage / 100.0);
			
			if(DEBUG) {
				System.out.printf("income range [%.2f .. %.2f) (size: %.2f), income: %.2f\n", stepMinIncome, stepMaxIncome, (stepMaxIncome - stepMinIncome), income);
				System.out.printf("stepIncome: %.2f, stepTaxPercentage: %.2f, stepTaxAmount: %.2f\n", stepIncomeAmount, stepTaxPercentage, stepTaxAmount);
				System.out.println();
			}
			
			return stepTaxAmount;
		}
		
		return 0.0;
	}
	
	// compute total tax amount by income
	static double compute(double income)
	{		
		// throw if invalid step data
		if(!isValidInternalState()) {
			throw new RuntimeException("invalid internal state / internal data");
		}
		
		// throw if invalid income
		if(income < 0) {
			throw new IllegalArgumentException("ugyldig inntekt");
		}
		
		// get number of tax steps
		int numberOfSteps = STEP_INCOMES.length;
				
		// declare tax amount accumulator
		double totalTaxAmount = 0.0;  				

		// accumulate tax for each step that has relevant income
		for(int i = numberOfSteps - 1; i >= 0; i--)
		{
			// get step min/max income 			
			double stepMinIncome = STEP_INCOMES[i];
			double stepMaxIncome = (i + 1 >= STEP_INCOMES.length) ? Double.MAX_VALUE : STEP_INCOMES[i + 1];
			
			// get step tax percentage 
			double stepTaxPercentage = STEP_PERCENTAGES[i]; 
			
			// compute step tax amount 
			double stepTaxAmount = computeStepTaxAmount(income, stepMinIncome, stepMaxIncome, stepTaxPercentage);

			// add to total tax amount
			totalTaxAmount += stepTaxAmount;
		}
		
		// return amount of tax to pay
		return totalTaxAmount;	
	}
}


/*
 * 
 */

public class O1 {
	// format norwegian currency to String
	static String formatCurrency(double amt) {
		NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("no", "NO"));
		return formatter.format(amt); 
	}
	
	// compare two doubles 
	static boolean areEqual(double a, double b, double epsilon) {
		return Math.abs(a - b) < epsilon;
	}	
	
	public static Double readOptionalDoubleFromInput() {
		String s = javax.swing.JOptionPane.showInputDialog(null, "Skriv inn inntekt (hel- eller flyttall).\n Husk: Du må legge til 99 øre for å få fasit-korrekte svar.\nSe kommentar fra topen av O1.java for forklaring", "Input", javax.swing.JOptionPane.INFORMATION_MESSAGE);
		return (s != null) ? Double.parseDouble(s) : null;
	}	
	
	// invoke tax calculator and print output 
	static double computeAndPrintTax(double income)	{
		double incomeBeforeTax = income;
		double taxAmount = 0.0;		
		
		try{
			// compute tax & print it
			taxAmount = TaxCalculator.compute(incomeBeforeTax);
			
			System.out.printf("Trinnskatt for %.2f er ca %s kroner\n", incomeBeforeTax, formatCurrency(taxAmount));			
		}catch(Exception e){
			System.out.printf("Feil: %s\n", e.getMessage());
		}

		return taxAmount;		
	}
	

	// invoke tax calculator that shows tax for each income when adjusted
	// with 0 or 0.99 NOK 
	public static void runDebuggingAndExplanationWithOutput() {
		/*
		 * kode for å vise at oppgavetekst må være feil eller at inntekt må justeres opp med noen
		 * ører for å få korrekt svar, vi har valgt 0.99 øre i tillegg som gir fasitsvar.
		 */
		
		// incomes, as given in trinnskatt.pdf before adjustments are made
		double incomes[] = new double[] {1350100.0, 938000.0, 670100.0, 292950.0, 208150.0, 200000.0};
		
		// conclusions, incomes[n] -> taxamount[n] (conclusion = engelsk for "fasit")  
		double conclusions[] = new double[] {121388.20, 52978.6, 16541.20, 1445.6, 1.7, 0.0}; 

		{
			/*
			 * vis hva som skjer hvis en bruker inputs fra trinnskatt.pdf ved å legge til 99 øre
			 */		
			System.out.println("Tax from income + 0.99 NOK");
			int numErrors = 0;		
			for(int i=0; i<incomes.length; i++) {
				double taxAmount = computeAndPrintTax(incomes[i] + 0.99);
				if(!areEqual(taxAmount, conclusions[i], 1e-2)) {
					System.out.printf("Utreknet skatt har feil mot fasit (%.2f vs %.2f)\n", taxAmount, conclusions[i]);
					numErrors++;
				}
			}
			System.out.printf("Det ble funnet %d feil mot fasit\n", numErrors);		
			System.out.println();
		}
		
		{
			/*
			 * vis hva som skjer hvis en bruker inputs fra trinnskatt.pdf uten å legge til 99 øre
			 */
			System.out.println("Tax from income + 0.00 NOK");
			int numErrors = 0;
			for(int i=0; i<incomes.length; i++) {
				double taxAmount = computeAndPrintTax(incomes[i] + 0.00);
				if(!areEqual(taxAmount, conclusions[i], 1e-2)) {
					System.out.printf("Utreknet skatt har feil mot fasit (%.2f vs %.2f)\n", taxAmount, conclusions[i]);
					numErrors++;
				}
			}
			System.out.printf("Det ble funnet %d feil mot fasit\n", numErrors);
			System.out.println();
		}
	}
	

	
	public static void runAssignment() {
		Double income = readOptionalDoubleFromInput();
		if(income != null) {
			computeAndPrintTax(income);
		}else {
			System.out.println("Bruker avbrøt innskriving");
		}
	}
	
	public static void main(String[] args) 
	{
		runAssignment();
		System.out.println();
		
		runDebuggingAndExplanationWithOutput();
	}
}