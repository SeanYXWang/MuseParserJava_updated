/*
 * 	This Method get the individual median from the incoming 4 Medians
 * 
 * 	example below: 
 * 	Median 1: 00 0000 0101 = 5, quantization = 1 from 16*0 * 8*0 * 4*0 * 2*0 * 1*1.
	Median 2: 10 0000 0010 = 2, quantization = 16 from 16*1 * 8*0 * 4*0 * 2*0 * 1*1.
	Median 3: 00 0000 0111 = 7, quantization = 1 from 16*0 * 8*0 * 4*0 * 2*0 * 1*1.
	Median 4: 10 0000 0100 = 4, quantization = 16 from 16*1 * 8*0 * 4*0 * 2*0 * 1*1.
	
	Reference: http://forum.choosemuse.com/forum/developer-forum/3290-bluetooth-connection-to-linux/page2
 */
package pack1;

public class GetIndividualMedian {

	public final static char mask0x3F = 0x3F;
	
	public static void main(String[] args) {
		
//		int[] m = {441, 75, 86, 491};
		// Xtra Test figures
//		int[] m = {616, 256, 325, 594};
//		int[] m = {369, 173, 159, 429};
//		int[] m = {519, 645, 645, 517};
//		int[] m = {518, 645, 645, 516};
//		int[] m = {519, 585, 645, 517};
		int[] m = {5, 514, 7, 517}; // example from http://forum.choosemuse.com/forum/developer-forum/5030-problem-with-parsing-compressed-eeg-packets
//		int[] m = {518, 771, 771, 516};
		
		int[] x = new int[4];
		x = getIndividualMedian(m);
		for(int i=0;i<4;i++)
		{
			System.out.println(x[i]);
		}
		
	}
		public static int[] getIndividualMedian(int[] m) {
		int[] a = new int[4];	
		//cast int to char
		for(int i=0;i<4;i++)
			a[i] =  m[i]; //cast received Medians in char
		
		for(int i=0;i<4;i++)
		{
			a[i] =  (a[i]&mask0x3F);
//			System.out.println((int)a[i]);
		}
		return a;
	}

}
