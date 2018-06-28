/*
 *  This method get the 10bit medians for each channel from 5 bytes
 * 
 * 	16th Feb 2015 Monday
 *  Futuer Task : use for loop 
 *  
 *  
 *  *******************************************************************
 * 	Haven't considered when median is 0.
 *  *******************************************************************
 *  
 */
package pack1;

public class Get10BitsMedians {

	public final static char mask0x03 = 0x03;
	public final static char mask0xFC = 0xFC;
	public final static char mask0x0F = 0x0F;
	public final static char mask0xF0 = 0xF0;
	public final static char mask0x3F = 0x3F;
	public final static char mask0xC0 = 0xC0;
	
	public static void main(String[] arg0)
	{
		char a1, a2, a3, a4, a5; 
		char b1, b2, b3, b4, b5, b6, b7, b8, b9, b10;
		int c1, c2, c3, c4, c5, c6, c7, c8, c9;
		
//		int[] medians = {185, 45, 97, 197, 122};
		/*
		 * Test figures
		 */
//		int[] medians = {93, 194, 72, 91, 128};
//		int[] medians = {104, 2, 84, 148, 148};
//		int[] medians = {185, 45, 97, 197, 122}; // Uncompressed EEG
//		int[] medians = {113, 181, 242, 73,107};
//		int[] medians = {5, 8, 120, 64, 129};
//		int[] medians = {7, 22, 90, 104, 129}; //this packet is the one after E0 7 22 90 104 129
//		int[] medians = {7, 38, 89, 104, 129 };
//		int[] medians = {5, 8, 120, 64, 129}; //example from http://forum.choosemuse.com/forum/developer-forum/5030-problem-with-parsing-compressed-eeg-packets
//		int[] medians = {6, 14, 60, 48, 129 };
		int[] medians = {5, 8, 120, 64, 129 };
		int[] answer = new int[4];
		answer = get10BitMedians(medians);
		for(int i=0;i<4;i++)
		{
			System.out.println(answer[i]);
		}
		
		
	}
		public static int[] get10BitMedians(int[] medians)
		{
			char a1, a2, a3, a4, a5; 
			char b1, b2, b3, b4, b5, b6, b7, b8, b9, b10;
			int c1, c2, c3, c4, c5, c6, c7, c8, c9;
			int[] c = new int[4];
		a1 =(char) medians[0];
		a2 =(char) medians[1];

		b1 =(char)(a2&mask0x03);

		c1 = (b1<<8)|a1;
//		System.out.println(c1);
		c[0] = c1;
		/*
		 * 
		 */
		b1 = (char)(a2&mask0xFC);
		c2 = b1 >> 2;
		a3 = (char) medians[2];
		b2 = (char)(a3&mask0x0F);
		c3 = b2 << 6;
		c2 = c2|c3;
//		System.out.println(c2);
		c[1] = c2;
		/*
		 * 
		 */
		b1 = (char)(a3&mask0xF0);
		c3 = b1 >> 4;
		a4 = (char) medians[3];
		b2 = (char)(a4&mask0x3F);
		c4 = b2 << 4;
		c3 = c3|c4;
//		System.out.println(c3);
		c[2] = c3;
		/*
		 * 
		 */
		b1 = (char)(a4&mask0xC0);
		c4 = b1 >> 6;
		a5 = (char) medians[4];
		
		c5 = a5 << 2;
		c4 = c4|c5;
//		System.out.println(c4);
		c[3] = c4;
		/*
		 * 
		 */
		return c;
	}
}
