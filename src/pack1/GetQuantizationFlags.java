/*
 * 	This Method get the individual Quantization from the incoming 4 Medians
 * 
 * 	example below: 
 * 	Median 1: 00 0000 0101 = 5, quantization = 1 from 16*0 * 8*0 * 4*0 * 2*0 * 1*1.
	Median 2: 10 0000 0010 = 2, quantization = 16 from 16*1 * 8*0 * 4*0 * 2*0 * 1*1.
	Median 3: 00 0000 0111 = 7, quantization = 1 from 16*0 * 8*0 * 4*0 * 2*0 * 1*1.
	Median 4: 10 0000 0100 = 4, quantization = 16 from 16*1 * 8*0 * 4*0 * 2*0 * 1*1.
	
	Reference: http://forum.choosemuse.com/forum/developer-forum/3290-bluetooth-connection-to-linux/page2
 */
package pack1;

public class GetQuantizationFlags {

//	public final static char mask0x03 = 0x03;
	public final static char mask0x40 = 0x40;
//	public final static char mask0x80 = 0x80;
//	public final static char mask0x100 = 0x100;
//	public final static char mask0x200 = 0x200;
	
	public static void main(String[] args) {
		// update the get10BitsMedians Method return a array of medians number
//		int[] m = {441, 75, 86, 491};
		//		int[] m = {518, 771, 771, 516};
		int[] m = {5, 514, 7, 517};

		//		int[] m = {5, 514, 7, 517}; // example from http://forum.choosemuse.com/forum/developer-forum/5030-problem-with-parsing-compressed-eeg-packets
		
		int[] x = new int[4];
		x = getQuantizationFlags(m);
		for(int i=0;i<4;i++)
		{
			System.out.println(x[i]);
		}
		
		
	}
	
	
	public static int[] getQuantizationFlags(int[] m) {


		int[] a = new int[4];
		int[] b = new int[4];
		int[] Quantization = new int[4];
		//cast int to char
		for(int i=0;i<4;i++)
		{
			a[i] = m[i]; //cast received Medians in char
			Quantization[i] = 1; // Initialize Quantization
		}
		for(int i=0;i<4;i++)
		{
			int x = 1;	
			for(int j=0;j<4;j++)
			{					
				b[j] = (a[i]&(mask0x40*x));
				x = (x*2);
				//				System.out.println((int)x);
				if(b[j] != 0)
					Quantization[i] =  (Quantization[i]*x);
			}
			//			System.out.println((int)Quantization[i]);
		}
		return Quantization;
	}

}
