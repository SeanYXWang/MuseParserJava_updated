/*
 * 		This Class get the "Quotient" for "difference"
 * 		20 Feb, 2015
 * 
 * 		Exceptions: when data is 0xFF (255 in decimal) so the quotient is not 8,
 * 					it must to see the next data when the first 0 is found
 */
package pack1;

public class GetQuotient {

	public static int getQuotient(char a) {
		
//		char a = 0xE1;
//		System.out.print((int)a);
		int quotient = 0;
		char mask = 0x80;
		
		// looking for the first 0 from MSB to find out the Quotient
//		for(int i=0;i<)
		
		while((a&mask)!=0)
		{
			mask = (char) (mask>>1);
			quotient++ ;
//			System.out.println((int)mask);
//			System.out.println("Quotient is: "+quotient);
		}
//		System.out.println("Quotient is: "+quotient);
		
		return quotient;
	}
	
	
	public static void main(String[] args) {
		char a = 0xFF;
		System.out.println(getQuotient(a));
	}

}
