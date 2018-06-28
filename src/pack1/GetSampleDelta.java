
package pack1;


public class GetSampleDelta {

	public static void main(String[] args) {
		
		
		// initalize received compressed sample data
//		int[] a = {8, 38, 150, 180, 80, 22, 150, 104, 83, 22, 80, 204, 
//				22, 243, 193, 216, 190, 86, 70, 97, 128, 22, 11, 122, 
//				217, 44, 95, 11, 31, 81, 192, 154, 233, 54, 101, 34, 
//				170, 194, 17, 138, 80};

		/*
		 * 	Test data: for easy test using the first 5 compressed sample delta
		 */
		int[] q = {190,245,231,215,238};  

		
		/*
		 * 	Test data: Test a exception that previous data is 255, so the quotient is not 8,
		 * 				but it must to look at the next data to find the first 0
		 */
//		int[] q = {255, 227, 150, 180, 80};
		
		/*
		 *  this is the test case, only 5 bytes 
		 *  #### #### #### #### #### #### #### #### 
		 *  in real data this number should be the number of bytes 
		 *  that received from getNumberOfBytesData class
		 */
		int theNumberOfBytes = 5;  // change here
		char[] m = new char[theNumberOfBytes];
		int quotientCount = 0; // give the quotient value
		int bitCheckCount = 0; // tells how many bit is checked. Gives the bit number
		int bitCheckCount_temp = 0; 
		
		int remainingBitsCount = 0;
		char remainingBitsValue = 0;
		int pre_remainingBitsCount = 0;
		
	
		
		/*
		 * 		Median should pass into this class for generating remainder 
		 */
		int median = 5; 
		
		//convert int to char
		for(int i=0;i<theNumberOfBytes;i++)
		{
			m[i] = (char) q[i]; 
		}	
		
		
		
		
		for(int i=0;i<5;i++)  // change here
		{
			/*
			 * 		First is check if there are bits left from previous bytes, 
			 * 		if yes, SHIFTed + ORed with next byte
			 */
			if(remainingBitsValue!=0)
			{
				m[i] = (char) (m[i]|(remainingBitsValue<<8)); // change here to m[i]
			}
			System.out.println((int)m[i]);
/*
 * Calculate Quotient
 */
			remainingBitsValue = 0; //reset
			bitCheckCount = 0; //reset
			bitCheckCount_temp = 0; //reset
			/*
			 * 	Unary encoding: looking for the first 0
			 */
			for(int j=0;j<8+remainingBitsCount;j++)
			{
				// Postion start with 0 -  Big Ending
				char thisBit = GetNextBitFromByte.getNextBitFromByte(m[i], j, remainingBitsCount); //Don't Forget change m[i]
//				System.out.println((int)thisBit);
				
				if((int)thisBit == 1)
				{
					quotientCount++;
					bitCheckCount++;
				}
				if((int)thisBit == 0)
					break;
//				System.out.println("quotient Count is "+quotientCount);
				
			}
			System.out.println("quotient Count is "+quotientCount);
			
			if(quotientCount==8)
			{
				/*
				 * quotient greater than 8
				 */
				int quotientCountPrevious = 8; //used to add to next byte quotientCount
				/*
				 * Then this byte Not Apply to Remainder calculation
				 * 
				 * 	????????????????  break or not?? start from next byte, back to original calclation
				 */
			}
			if(quotientCount==15)
			{
				//Elias Gamma Encoding 
				/*
				 * 		same situation as above
				 */
			}
			
			// situation that quotientCount < 8 
/*
*  Calcualte Remainder
*/
//			System.out.println("Remainder is ");
			
			/*
			 * Truncated binary encoding assigns the first u symbols codewords of length k 
			 * and then assigns the remaining median - u symbols the last median - u codewords of 
			 * length k + 1. Because all the codewords of length k + 1 consist of an unassigned 
			 * codeword of length k with a "0" or "1" appended, the resulting code is a prefix code.
			 */
			int remainder = 0;
			int k = (int) Math.floor(Math.log10(5)/Math.log10(2)); //here 5 is median (need replace)
			int u = (int) (Math.pow(2, (k+1))-median);
//			System.out.println("k is "+k+"    u is "+u);
			int[] buf = new int[k];
			int[] buf2 = new int[k+1];
			char temp_b = 0;
			char temp_c = 0;
			for(int i1=0;i1<k;i1++)
			{
				char thisBit = GetNextBitFromByte.getNextBitFromByte(m[i], bitCheckCount+1+i1,remainingBitsCount); // Change m[i]
				buf[i1] = thisBit;
//				System.out.println((int)thisBit);
			}
//			for(int i=0;i<buf.length;i++)
//			{
//				System.out.println(buf[i]);
//			}
			for(int i1=0;i1<k;i1++)
			{
				temp_b = (char) (buf[i1]<<(k-(i1+1)));
				temp_c = (char) (temp_c|temp_b);
				bitCheckCount_temp++;
			}
			bitCheckCount_temp = bitCheckCount+1+bitCheckCount_temp;
//			System.out.println("bit Check Count is "+ bitCheckCount_temp);
			remainder = (int)temp_c;
//			System.out.println("Remainder is "+ (int)temp_c);
			// right now here I get remainder for k length. Next is to check if the acutal remainder is k+1 length long
			if(temp_c>=u)
			{
				k++;
				temp_c = 0; // reset temp_c ;
				for(int i1=0;i1<k;i1++)
				{
					char thisBit = GetNextBitFromByte.getNextBitFromByte(m[i], bitCheckCount+1+i1,remainingBitsCount); //Change m[i]
					buf2[i1] = thisBit;
//					System.out.println((int)thisBit);
				}
				for(int i1=0;i1<k;i1++)
				{
					temp_b = (char) (buf2[i1]<<(k-(i1+1)));
					temp_c = (char) (temp_c|temp_b);
//					System.out.println((int)temp_b);
				}
				remainder = (int)temp_c-u;
//				System.out.println("Remainder is "+ ((int)temp_c-u));
				bitCheckCount_temp++;
			}
			System.out.println("Remainder is "+remainder);
//			System.out.println("--------  bitCheckCount is "+bitCheckCount_temp);
//			System.out.println("********  Pre remainingBitsCount is "+pre_remainingBitsCount);
			quotientCount = 0; //reset count
			
/*
 * 	Get the sign bit
 */
			int sign = 1;
			char thisBit = GetNextBitFromByte.getNextBitFromByte(m[i], bitCheckCount_temp,remainingBitsCount); //change m[i]
			
			if(thisBit == 0)
				sign = -1;
			System.out.println("Sign is "+sign);
			
			/*
			 * 		Calculate Difference
			 */
			
			bitCheckCount_temp++;
//			System.out.println("bitCheckCount is "+bitCheckCount);
			
			/*
			 *  Now save the unprocessed bits (rest bits) in a buffer, SHIFTed + ORed with next byte 
			 *  Make sure the buffer is not over 8-bit, if it is, read byte for loop breaked and re-calculate
			 *  the length of the rest bytes
			 */
			remainingBitsCount = 8 + remainingBitsCount - bitCheckCount_temp;
			System.out.println("********  remainingBitsCount is "+remainingBitsCount);
			if(remainingBitsCount!=0)
			{
				int[] buffer = new int[remainingBitsCount];
				char temp_d = 0;
				for(int i1=0;i1<remainingBitsCount;i1++)
				{
					char thisBitValue = GetNextBitFromByte.getNextBitFromByte(m[i], bitCheckCount_temp+i1,pre_remainingBitsCount); //chang m[i] here
					buffer[i1] = thisBitValue;
//									System.out.println("£££££££ thisBitValue"+(int)thisBitValue);
//									System.out.println("£££££££ bitCheckCount_temp "+(int)bitCheckCount_temp);
				}
				for(int i1=0;i1<remainingBitsCount;i1++)
				{
					temp_d = (char) (buffer[i1]<<(remainingBitsCount-(i1+1)));
					remainingBitsValue = (char) (remainingBitsValue|temp_d);
					//				System.out.println((int)temp_d);
				}
				System.out.println("remaining Bits Value in decimal "+(int)remainingBitsValue);
				pre_remainingBitsCount = remainingBitsCount;
			}
			
		}
		
	}

}
