package pack2;

import java.sql.Struct;

public class GetDifference {

	private static int quotientCount = 0;
	private static int quotient = 0;
	private static int remainder = 0;
	private static int sign = 1;
	private static boolean enableGetQuotient = true;
	private static boolean enableGetRemainder = false;
	private static boolean enableGetSign = false;
	private static boolean enableGetQuotient_Post = false;
	private static boolean enableGetRemainder_Post = false;
	private static boolean enableGetSign_Post = false;
	private static int localCount = 0;
	private static boolean need1BitMore = false;
	private static int count16 = 0;
	private static int count4 = 0;
	
//    public class Frame {
//    	public int[] channel;
//    	public int[] samples;
//    	
//    	public Frame(){
//    		channel = new int[4];
//    		samples = new int[16];
//    		
//    	}
//    };
	
	public static void main(String[] args) {
		
		/*
		 * Median1 = 7 --> 0 = 00, 1 = 010, 2 = 011, 3 = 100, 4 = 101, 5 = 110, 6 = 111.
		 * Median2 = 5 --> 0 = 00, 1 = 01, 2 = 10, 3 = 110 and 4 = 111.
		 * Median3 = 5 --> 0 = 00, 1 = 01, 2 = 10, 3 = 110 and 4 = 111.
		 * Median4 = 5 --> 0 = 00, 1 = 01, 2 = 10, 3 = 110 and 4 = 111.
		 */
		
		//These values are passed in 
//		int[] a = {108, 38, 150, 180, 80, 22, 150, 104, 83, 22, 80, 204, 
//				22, 243, 193, 216, 190, 86, 70, 97, 128, 22, 11, 122, 
//				217, 44, 95, 11, 31, 81, 192, 154, 233, 54, 101, 34, 
//				170, 194, 17, 138, 80};
//		int[] a = {165,84,13,231,39,87,23,84,186,202,165,164,86,82,
//					207,38,205,149,138,45,41,50,148,53,90,69,93,169,
//					114,144,226,176,133,98,214,59,133,0}; 
		//Runner's example data
		int[] a = {225,240,197,199,237,123,7,251,99,78,59,77,60,243,
				172,108,76,77,76,3,113,46,97,145,17,123,80,245,114,
				103,158,220,48,74,133,193,88,201,35,104,200,109};
		int[] median = {5,2,7,5}; //These values are passed in 
		int[] quantization = {1, 16, 1, 16}; //These values are passed in 
		int bitNumberOfLength = 336;
		int[] initialEEG = {605, 560, 436, 513};  //These values are passed in 
		
		int[][] sample = new int[4][64];
		sample = getSampleDeltas(a, median, quantization, initialEEG, bitNumberOfLength);
		System.out.println("****************************");
		System.out.println("****************************");
		System.out.println("****************************");
		System.out.println("****************************");
		System.out.println("****************************");
		for(int j=0;j<4;j++)
		{
			for(int i=0;i<16;i++)
			{
				System.out.println(sample[j][i]+" ");
			}
		}

	}
	public static int[][] getSampleDeltas(int[] a, int[] median, int[] quantization, int[] initialEEG, int bitNumberOfLength) {

//		Frame parsed_data = new Frame();

		int x = 0; // count 0-3, select median
//		int[] a = {190,245,38,8,100};
		int length = a.length;
		int[][] b = Convert.to2DBitArray(a, length);
//		int[][] c = Convert.reverse(b, length, 8);
//				for(int k=0;k<a.length;k++)
//					{
//						for(int i=0;i<8;i++)
//						{
//							System.out.print(b[k][i]);
//						}
//					}
		
//		Refer to Truncated Binary Encoding 
		int k = (int) Math.floor(Math.log10(median[0])/Math.log10(2)); //here 5 is median (need replace)
		int u = (int) (Math.pow(2.0, (k+1))-median[0]);
//		System.out.println(" k="+k);
		int[] remainderArray = new int[k];
		int[] remainderArrayExtra1Bit = new int[k+1];
			
		int TestCounter = 1; //debug use
		
		int bitCount = 0;
		


		int[] tempInitialEEG = new int[4];
		for(int i=0;i<4;i++)
		{
			tempInitialEEG[i]=initialEEG[i];
		}
		int[][] differences = new int[4][16];

		
		for(int j=0;j<length;j++)
		{
			for(int i=0;i<8;i++)
			{
//				Disable Remainder and Sign bit calculation, Enable Quotient			
//				System.out.println("(debug) process c["+j+"]["+i+"]");
//	REMINDER: Intial enableGetQuotient = ture ; enableGetSign_Post = false
				if(enableGetQuotient == true && enableGetQuotient_Post == true )
				{
					enableGetSign_Post = false;
					enableGetRemainder = true;
					enableGetQuotient = false;
				}
				
				if((b[j][i])==1 && enableGetQuotient==true)
				{
					quotientCount++;
					quotient = quotientCount;
					
				}
				if((b[j][i])==0 && enableGetQuotient==true)
				{
//					enableGetQuotient = false;
//					enableGetRemainder = true;
					enableGetQuotient_Post = true;
					
//					System.out.println("quotient is "+quotient);
					quotientCount = 0;
//					quotient = 0;
				}
				
//				When Quotient obtained, Start Remainder calculation, disable Quotient	
				
				if(enableGetRemainder == true && enableGetRemainder_Post == true)
				{
//					System.out.println("(debug) AM I HERE BEFORE?");
					enableGetQuotient_Post = false;
					enableGetRemainder = false;
					enableGetSign = true;
					k = (int) Math.floor(Math.log10(median[x])/Math.log10(2)); //here 5 is median (need replace)
//					u = (int) (Math.pow(2.0, (k+1))-median);
					remainderArray = new int[k];
					remainderArrayExtra1Bit = new int[k+1];
				}

//				//Debug
//				if(bitCount==165)
//					System.out.println("Break Pont Here");
//				//End Debug
				
				if(enableGetRemainder==true && median[x]==2)
				{
					remainder = b[j][i];
					enableGetRemainder_Post = true;
					localCount = 0;
//					System.out.println("M2  remainder is "+remainder+"  LOCAL COUNT = "+localCount);
				}
				if(enableGetRemainder==true && median[x]==1)
				{
					remainder = 0;
					enableGetRemainder_Post = true;
					localCount = 0;
//					System.out.println("M1  remainder is "+remainder);
				}
				
				if(enableGetRemainder==true && localCount<k && median[x]!=2 && median[x]!=1)
				{
//					System.out.println("(debug) I am Here");
//					System.out.println("(debug) b["+j+"]["+i+"] is "+b[j][i]);
					if(need1BitMore == false)
						remainderArray[localCount] = b[j][i];
					else
						remainderArrayExtra1Bit[localCount] = b[j][i];
					localCount++;
					if(localCount==k && need1BitMore == false)
					{
						//covert remainderArray[localCount] to actual remainder
						remainderArray = Convert.reverse(remainderArray, remainderArray.length);
						remainder = (char) Convert.toInt(remainderArray, remainderArray.length);
						if(remainder >= u)
						{
							need1BitMore = true;
							k++;
							remainderArray = Convert.reverse(remainderArray, remainderArray.length);
//							Increase old array size by 1 need to create a new array and copy old array values into new array 
							System.arraycopy(remainderArray, 0, remainderArrayExtra1Bit, 0, remainderArray.length);
						}
						else
						{
//							enableGetRemainder = false;
//							enableGetSign = true;
							enableGetRemainder_Post = true;
							localCount = 0;
//							System.out.println("remainder is "+remainder);
							
						}
						
					}
					if(localCount==k && need1BitMore == true)
					{
						remainderArrayExtra1Bit = Convert.reverse(remainderArrayExtra1Bit, remainderArrayExtra1Bit.length);
						remainder = (char) (Convert.toInt(remainderArrayExtra1Bit, remainderArrayExtra1Bit.length) - u);
						need1BitMore = false;
//						enableGetRemainder = false;
						enableGetRemainder_Post = true;
//						enableGetSign = true;
						localCount = 0;
//						System.out.println("1bit extra remainder is "+remainder);
					}
				}
				
//				if( enableGetSign == true && enableGetSign_Post == true)
//				{
//					enableGetRemainder_Post = false;
//					enableGetSign = false;
//					enableGetQuotient = true;
//				}
//				When Remainder obtained, Start Sign bit calculation, disable Remainder
				if(enableGetSign == true)
				{
					if(b[j][i] == 0)
						sign = 1;
					else
						sign = -1;
					enableGetSign = false;
					enableGetRemainder_Post = false;
					enableGetSign_Post = true;
					enableGetQuotient = true;
//					System.out.println("Sign is "+sign);
//					System.out.println("******Test Counter is "+TestCounter++);
//					deltas = (quotient * median + remainder) * sign * quantization
					differences[x][count16] = tempInitialEEG[x] + (quotient * median[x] + remainder) * sign * quantization[x];
					
//					parsed_data.channel[x].sam
					tempInitialEEG[x] = differences[x][count16];
					
//					writer.println(differences[x][count16]);
					
//					System.out.println("****** Sample Delta CH["+ x +"]= "+differences[x][count16]);
					
					
					// Print out only single channel that being selected
					if(x==0)
					{
						System.out.println(differences[x][count16]+" ");
					}
					
					//THis line print all Four channel value
//					System.out.println("CH["+x+"]["+count16+"]"+differences[x][count16]+" ");
					
					
					
					quotient = 0; //reset quotient here NOT in the Quotient Calculation
					count16++;
					if(count16 == 16)
					{
						x++;
						count16 = 0;
						
						if(x==4)
							x = 0;
						else
						{
							k = (int) Math.floor(Math.log10(median[x])/Math.log10(2)); 
							u = (int) (Math.pow(2.0, (k+1))-median[x]);
							remainderArray = new int[k];
							remainderArrayExtra1Bit = new int[k+1];
						}
					}
				}
				
				
				/*
				 * Another If condition required for ignoring extra bit(s), due to length byte had been rounded up 
				 */
				bitCount++;
//				System.out.println("(debug)******bitCount Counter is "+bitCount);
				if(bitCount == bitNumberOfLength)
					break;
			}
//			System.out.print(" "+quotientCount);
		}
		return differences;

	}

}
