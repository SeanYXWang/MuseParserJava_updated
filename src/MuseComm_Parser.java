import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.Scanner;

import pack1.Get10BitsMedians;
import pack1.GetIndividualMedian;
import pack1.GetNumberOfBytesData;
import pack1.GetQuantizationFlags;
import pack2.GetDifference;

public class MuseComm_Parser {

	//private static SerialPort serialPort = new SerialPort("/dev/tty.Muse-RN-iAP");

	public static boolean found_0xE0 = false;
	public static boolean found_0xC0 = false;
	
	private static final int bufferSize = 200; // 5min.txt => 2123533
//	public static int[] buffer = new int[bufferSize];

	public static int[] unompressedEEG = new int[4];

	public static int[] bufferE0 = new int[5];
	public static int countC0 = 0;
	public static int countPosition = 0;

	public static int count_10 = 10;
	
	public static int[] medianBuffer = new int[5];
	public static int j = 0;

	private static boolean receive5Median = false;
	public static int[] median = new int[4];
	public static int[] quantization = new int[4];

	private static boolean readC0Median = false;
	private static boolean readC0Length = false;
	public static int[] lengthBuffer = new int[2];
	public static int c0LengthInBits = 0;
	public static int c0LengthInBytes = 0;
	private static boolean readC064SampleDelta = false;
	private static boolean lookingForC0 = false;
	private static boolean medianIsEnough = false;
	private static boolean readC0MedianComplete = false;
	private static boolean lengthBytesIsEnough = false;
	private static boolean readC0LenghBytesComplete = false;
	private static boolean sampleDeltaIsEnough = false;
	
	public static int[] c064SampleDelta; //store the C0 raw eeg datas
	public static int[][] sample = new int[4][16];
	public static int p = 0;
	private static int w = 0;
	private static boolean First_found_0xE0 = false;
	private static boolean First_found_0xE0_flag2 = false;
	private static boolean Not_First_found_0xE0 = false;
	private static int count_5 = 6;
	private static int[] c064SampleDelta_pre;
	private static int[] c064SampleDelta_post;
	private static boolean readC064SampleDelta_pre = false;
	private static boolean readC064SampleDelta_post = false;
	private static int lengthBuffer_pre;
	private static int lengthBuffer_post;
	private static boolean readlengthBuffer_pre = false;
	private static boolean readlengthBuffer_post = false;
	private static int[] medianBuffer_pre;
	private static int[] medianBuffer_post;
	private static boolean readmedianBuffer_pre = false;
	private static boolean readmedianBuffer_post = false;
	
	private static int c064SampleDelta_pre_length = 0;
	private static int c064SampleDelta_post_length = 0;
	private static int medianBuffer_pre_length = 0;
	private static int medianBuffer_post_length = 0;
	private static boolean E0isEnough = false;
	private static boolean lookingForE0 = false;
	private static boolean ignore = false;
	private static boolean readE0Complete = false;
	private static boolean readE0buffer_pre = false;
	private static boolean readE0buffer_post = false;
	private static int[] bufferE0_pre;
	private static int[] bufferE0_post;
	private static int bufferE0_pre_length = 0;
	private static int bufferE0_post_length = 0;
	
	private static int[][][] results_current = new int[1][4][16];
    private static int[][][] results_old = new int[1][4][16];
    private static int[][][] results_new = new int[10][4][16];
    private static int packetNum = 0;
	
	
	public static int[][][] museBTDataReceiver(int[] buffer){
		
		for(int i=0;i<buffer.length;i++)
		{
//			countPosition++;
//			if(countPosition==buffer.length)
//				countPosition = 0;
//			
//			System.out.println("         (debug) Position = "+countPosition);
//			System.out.println("         (debug) Current Raw value = "+buffer[i]);
//			System.out.println("         (debug) i = "+i);
			
			
			if(found_0xE0==false)
			{
				//looking for FF FF AA 55 [Looking for this Syn package only ONCE by starting receving data]
				//First_found_0xE0_flag2==true this flag controls to look for FF FF AA 55 only runs once
				if(buffer[i]==0xFF && First_found_0xE0_flag2==false)
					if(buffer[i+1]==0xFF)
						if(buffer[i+2]==0xAA)
							if(buffer[i+3]==0x55)
							{
//										System.out.println();
//										System.out.println(" ---- Find FFFFAA55 ----- ");// found start
								
								if(buffer[i+4]==0xE0)
								{
									
//											System.out.println("&&&&  find E0   &&&&");
									if(i+5+5<buffer.length)
									{
										count_10 = 0;//reset
										found_0xE0 = true;
										First_found_0xE0 = true;
										First_found_0xE0_flag2 = true;
										
//										count_10++; // count_10: 255 255 170 85 224 X X X X X 
									
										bufferE0[0]=buffer[i+4+1];
										bufferE0[1]=buffer[i+4+2];
										bufferE0[2]=buffer[i+4+3];
										bufferE0[3]=buffer[i+4+4];
										bufferE0[4]=buffer[i+4+5];
										unompressedEEG = Get10BitsMedians.get10BitMedians(bufferE0);
//										System.out.println("(debug) E0 found success ");
									}
									else
									{
										/*
										 *  Note: Should I consider the buffer not contain enough uncompressed EEG data
										 *  ANS: No. Set the the buffer that read in the raw data big enough
										 */
									}
								}
							}
				//confirm delimiter 0xE0
				if(buffer[i]==0xE0 && First_found_0xE0_flag2==true && ignore == false)
				{
					lookingForE0 = true;
					ignore = true;
					continue;
				}
				// Check buffer length that contains enough 0xE0 packets 
				if( lookingForE0 == true)
				{
					if(i+5<=buffer.length )
					{
						E0isEnough  = true;
						lookingForE0 = false;
						readE0Complete = false;
						//debug
//						System.out.println("       (debug) Starting fill up bufferE0 ...");
						//end debug
					}
					else
					{
						E0isEnough = false;
						lookingForE0 = false;
						readE0Complete = false;
						readE0buffer_pre = true;
						bufferE0_pre_length = buffer.length-i;
						bufferE0_post_length = 5-(buffer.length-i);
						bufferE0_pre = new int[bufferE0_pre_length];
						bufferE0_post = new int[bufferE0_post_length];
						
						//debug
//						System.out.println("       (debug) Read first part of E0 packets ...");
						//end debug
					}
				}
				if(E0isEnough==true)
				{
					bufferE0[j]=buffer[i];
					j++;
					if(j==5)
					{
						j=0;
						found_0xE0 = true;
						lookingForC0 = true;
						
						ignore = false;
						E0isEnough = false;
						readE0Complete = true;
						
						unompressedEEG = Get10BitsMedians.get10BitMedians(bufferE0);
						//debug
//						System.out.println("       (debug) Not first E0 found success ...");
						//end debug		
						continue;
					}						
				}
				if(E0isEnough==false  && readE0buffer_pre==true)
				{
					bufferE0_pre[j]=buffer[i];
					j++;
					if(j==bufferE0_pre_length)
					{
						j = 0;
						readE0buffer_pre = false;
						readE0buffer_post = true;
						continue;
					}
				}
				if(E0isEnough==false  && readE0buffer_post==true)
				{
					bufferE0_post[j]=buffer[i];
					j++;
					if(j==bufferE0_post_length)
					{
						j = 0;
						
						int[] bufferE0_combined = new int[5];
						System.arraycopy(bufferE0_pre, 0, bufferE0_combined, 0, bufferE0_pre.length);
						System.arraycopy(bufferE0_post, 0, bufferE0_combined, bufferE0_pre.length, bufferE0_post.length);
						unompressedEEG = Get10BitsMedians.get10BitMedians(bufferE0_combined);
						found_0xE0 = true;
						lookingForC0 = true;
						readE0buffer_post = false;
						
						ignore = false;
						E0isEnough = false;
						readE0Complete = true;
						
						//debug
//						System.out.println("       (debug) COMBINED  E0 found success ...");
						//end debug	
						
						continue;
					}
				}

			}
			
			//  skip -> 255 255 170 85 224 X X X X X 192, then i is point at the next element after 192
			if(count_10<11 & First_found_0xE0==true) 
			{
				count_10++;
				lookingForC0 = true;
				if(count_10==11)
				{
					First_found_0xE0 = false;
				}
			}
			else
			{
				// Note: didnt check buffer length here, may happen that the buffer end up before 192
			}
			
			if(((count_10)==11||(count_5==6)) && lookingForC0==true && buffer[i]==0xC0 && E0isEnough==false)
			{
				readC0Median = true;
				lookingForC0 = false;
				// read in data from device again?
				if(i+5<=buffer.length)
				{
					medianIsEnough = true;
					continue;
				}
				// EXCEPTION case
				else
				{
					medianIsEnough = false;
					medianBuffer_pre_length = buffer.length-i;
					medianBuffer_post_length = 5-(buffer.length-i);
					medianBuffer_pre = new int[medianBuffer_pre_length];
					medianBuffer_post = new int[medianBuffer_post_length];
					readmedianBuffer_pre = true;
					continue;
				}
			}

			// this checks if sync packets appears, and ignore them
			// count_5==6 prevent FF AA 55  appears when read in the E0 buffer
			if(count_10==11 && lookingForC0==true && buffer[i]==0xFF && E0isEnough==false ) {
				//debug
//				System.out.println("ignore FF");
				//end debug
				continue;
			}
			if(count_10==11 && lookingForC0==true && buffer[i]==0xFF && E0isEnough==false ) {
				//debug
//				System.out.println("ignore FF");
				//end debug
				continue;
			}
			if(count_10==11 && lookingForC0==true && buffer[i]==0xAA && E0isEnough==false ) {
				//debug
//				System.out.println("ignore AA");
				//end debug
				continue;
			}
			if(count_10==11 && lookingForC0==true && buffer[i]==0x55 && E0isEnough==false ) {
				//debug
//				System.out.println("ignore 55");
				//end debug
				continue;
			}

			

			if(readC0MedianComplete == true)
			{
				readC0Length = true;
				readC0MedianComplete = false;
				if(i+2<=buffer.length)
				{
					lengthBytesIsEnough = true;
				}
				else
				{
					lengthBytesIsEnough = false;
					readlengthBuffer_pre = true; //enable read first half 
				}
			}
			
			//found C0, start process
			// count_5==6 prevent 192 appears when read in the E0 buffer
			if(readC0Median==true && medianIsEnough==true )
			{
				//debug
//				System.out.println("<<Position 1>>   j is = "+j+" i is = "+i+"  buffer["+i+"]="+buffer[i]);
				//end debug
				medianBuffer[j]=buffer[i];
				j++;
				if(j==5)
				{
//					//debug
//					for(int t=0;t<5;t++)
//					{
//						System.out.print(" "+medianBuffer[t]);
//					}
//					//end debug
					j=0;

					median = GetIndividualMedian.getIndividualMedian(Get10BitsMedians.get10BitMedians(medianBuffer));
					//debug
//					for(int t=0;t<4;t++)
//					{
//						System.out.print(" "+median[t]);
//					}
					//end debug
					quantization = GetQuantizationFlags.getQuantizationFlags(Get10BitsMedians.get10BitMedians(medianBuffer));
					//debug
//					for(int t=0;t<4;t++)
//					{
//						System.out.print(" "+quantization[t]);
//					}
////				//end debug
					readC0Median = false;	
					readC0MedianComplete = true;
					medianIsEnough = false;
					
					//debug
//					System.out.println("I am out of read medianBuffer ..... ");
//					System.out.println("<<Position 1.1>>   j is = "+j+" i is = "+i+"  buffer["+i+"]="+buffer[i]);		
					//end debug
				}
			}
			if(readC0Median==true && medianIsEnough==false && readmedianBuffer_pre==true)
			{
				//buffer elements not enough. C0 4-10bits medians didnt read enough 
				//combine next read add two buffer together and than continue process

				
				//debug
//				System.out.println("<<Position 2>>   j is = "+j+" i is = "+i+"  buffer["+i+"]="+buffer[i]);
				//end debug
				
				
				//read first half of median
				medianBuffer_pre[j] = buffer[i];
				j++;
				if(j==medianBuffer_pre_length)
				{
					j = 0;
					readmedianBuffer_pre = false;
					readmedianBuffer_post = true;
					
					//debug
//					System.out.println("<<Position 2.1>>   j is = "+j+" i is = "+i+"  buffer["+i+"]="+buffer[i]);
					//end debug
					
					continue;
				}
			}
			if(readC0Median==true && medianIsEnough==false && readmedianBuffer_post==true )
			{
				//buffer elements not enough. C0 4-10bits medians didnt read enough 
				//combine next read add two buffer together and than continue process

				//debug
//				System.out.println("<<Position 3>>   j is = "+j+" i is = "+i+"  buffer["+i+"]="+buffer[i]);
				//end debug
				
				//read first half of median
				medianBuffer_post[j] = buffer[i];
				j++;
				if(j==medianBuffer_post_length)
				{
					j = 0;
					readmedianBuffer_post = false;
					int[] medianBuffer_combined = new int[5];
					System.arraycopy(medianBuffer_pre, 0, medianBuffer_combined, 0, medianBuffer_pre.length);
					System.arraycopy(medianBuffer_post, 0, medianBuffer_combined, medianBuffer_pre.length, medianBuffer_post.length);
					
					median = GetIndividualMedian.getIndividualMedian(Get10BitsMedians.get10BitMedians(medianBuffer_combined));
					quantization = GetQuantizationFlags.getQuantizationFlags(Get10BitsMedians.get10BitMedians(medianBuffer_combined));
					readC0Median = false;	
					readC0MedianComplete = true;
					medianIsEnough = false;
					
					//debug
//					System.out.println("<<Position 3.1>>   j is = "+j+" i is = "+i+"  buffer["+i+"]="+buffer[i]);
					//end debug
					
					continue;
				}
			}
			
			if(readC0LenghBytesComplete==true)
			{
				readC064SampleDelta = true;
				readC0LenghBytesComplete = false;
				
				// check the C0 packet has enough 64 sample delta, EXCEPTION will happen to the last C0 packet in the buffer
				if(c0LengthInBytes+i<=buffer.length)
				{
					sampleDeltaIsEnough = true;
					c064SampleDelta = new int[c0LengthInBytes];
				}
				// EXCEPTION case
				else
				{
					sampleDeltaIsEnough = false;
					c064SampleDelta_pre_length = buffer.length-i;
					c064SampleDelta_post_length  = c0LengthInBytes-(buffer.length-i);
					c064SampleDelta_pre = new int[c064SampleDelta_pre_length];
					c064SampleDelta_post = new int[c064SampleDelta_post_length];
					
//					System.out.println("c064SampleDelta_pre = "+(buffer.length-i));
//					System.out.println("c064SampleDelta_post = "+(c0LengthInBytes-(buffer.length-i)));
//					System.out.println("buffer length = "+buffer.length);
//					System.out.println("current i value = "+ i);
//					System.out.println("current c0LengthInBytes value = "+ c0LengthInBytes);
					
					readC064SampleDelta_pre = true;
				}
			}
			
			if(readC0Length==true && lengthBytesIsEnough==true)
			{
				//debug
//				System.out.println("<<Position 4>>   j is = "+j+" i is = "+i+"  buffer["+i+"]="+buffer[i]);
				//end debug
				
				lengthBuffer[j]=buffer[i];
				j++;
				if(j==2)
				{
					j=0;
					readC0Length = false;
					c0LengthInBits = GetNumberOfBytesData.getNumberOfBitsData(lengthBuffer);
					c0LengthInBytes = GetNumberOfBytesData.getNumberOfBytesData(lengthBuffer);
					readC0LenghBytesComplete = true;
					lengthBytesIsEnough = false;
					
					//debug
//					System.out.println("   c0LengthInBytes Position 1   c0LengthInBytes is = "+c0LengthInBytes);
					//end debug
					//debug
//					System.out.println("<<Position 4.1>>   j is = "+j+" i is = "+i+"  buffer["+i+"]="+buffer[i]);
					//end debug
					
				}
			}
			if(readC0Length==true && lengthBytesIsEnough==false &&readlengthBuffer_pre==true)
			{
				//buffer elements not enough. lengh bits didnt read enough
				lengthBuffer_pre = buffer[i];
				readlengthBuffer_pre = false;
				readlengthBuffer_post = true;
				continue;
			}
			if(readC0Length==true && lengthBytesIsEnough==false &&readlengthBuffer_post==true)
			{
				//buffer elements not enough. lengh bits didnt read enough
				lengthBuffer_post = buffer[i];
				readlengthBuffer_post = false;
				int[] lengthBuffer_combined = new int[2];
				lengthBuffer_combined[0] = lengthBuffer_pre;
				lengthBuffer_combined[1] = lengthBuffer_post;
				
				readC0Length = false;
				c0LengthInBits = GetNumberOfBytesData.getNumberOfBitsData(lengthBuffer_combined);
				c0LengthInBytes = GetNumberOfBytesData.getNumberOfBytesData(lengthBuffer_combined);
				readC0LenghBytesComplete = true;
				lengthBytesIsEnough = false;
				
				//debug
//				System.out.println("c0LengthInBytes Position 2   c0LengthInBytes is = "+c0LengthInBytes);
				//end debug
				
				continue;
			}
			
			if(readC064SampleDelta==true && sampleDeltaIsEnough==true)
			{
				
				//debug
//				System.out.println("<<Position 5>>   j is = "+j+" i is = "+i+"  buffer["+i+"]="+buffer[i]);
				//end debug
				
				
				c064SampleDelta[j] = buffer[i];
				j++;
				if(j==c0LengthInBytes)
				{
					//debug
//					System.out.println("<<Position 5.1>>   j is = "+j+" i is = "+i+"  buffer["+i+"]="+buffer[i]);
					//end debug
					//debug
//					System.out.println("   c0LengthInBytes Position 3   c0LengthInBytes is = "+c0LengthInBytes);
					//end debug
					
//					//debug
//					for(int t=0;t<unompressedEEG.length;t++)
//					{
//						System.out.print(" "+unompressedEEG[t]);
//					}
//					//end debug
					//debug
//						System.out.print(" c0LengthInBits= "+c0LengthInBits);
					//end debug
					//feed into c0 parser
					sample = GetDifference.getSampleDeltas(c064SampleDelta, median, quantization, unompressedEEG, c0LengthInBits);
					
					if(sample.length>0)
                    {
                        results_current[0] = sample;
                        System.arraycopy(results_current, 0, results_new, packetNum, results_current.length);

                        packetNum++;
                        sample = new int[4][16];
                    }
					w++;
//					System.out.println("(debug) One C0 Packet Parsed " + w);
					
					j=0;//reset j for next C0 packet
					
					//debug
//					System.out.println("<<Position 5.2>>   j is = "+j+" i is = "+i+"  buffer["+i+"]="+buffer[i]);
					//end debug
					
					lookingForC0 = true;
					readC064SampleDelta = false;
					sampleDeltaIsEnough = false;
					countC0++;
					if(countC0==8)
					{
						//then starts looking for next E0
						lookingForC0 = false;
						found_0xE0 = false; // 8 0xC0 packets complete parsing, enable looking for 0xE0 (heart beat packet)
						countC0 = 0; //reset, accept new 8 0xC0 packets
						p++;
//						System.out.println("8 0xCO packet parsed " + p);
						
					}
					
				}

			}
			if(readC064SampleDelta==true && sampleDeltaIsEnough==false && readC064SampleDelta_pre==true)
			{
				// buffer is not long enough for reset data
				//debug
//				System.out.println("<<Position 6>>   j is = "+j+" i is = "+i+"  buffer["+i+"]="+buffer[i]);
				//end debug
				c064SampleDelta_pre[j] = buffer[i];
				j++;
				if(j==c064SampleDelta_pre_length)
				{
//					System.out.println(" j = " +j);
					j = 0;
					readC064SampleDelta_pre = false;
					readC064SampleDelta_post = true;
					
					//debug
//					System.out.println("<<Position 6.1>>   j is = "+j+" i is = "+i+"  buffer["+i+"]="+buffer[i]);
					//end debug
					
					continue; // up to here the current 900 element buffer is read complete,
							  /*
							   *  the next step is going to wait the next 900 element buffer read, and save 
							   *  whatever the uncompleted previous C0 datas in to 
							   *  c064SampleDelta_post[c0LengthInBytes-buffer.length-i] 
							   */
				}
			}
			if(readC064SampleDelta==true && sampleDeltaIsEnough==false && readC064SampleDelta_post==true)
			{
				//debug
//				System.out.println("<<Position 7>>   j is = "+j+" i is = "+i+"  buffer["+i+"]="+buffer[i]);
				//end debug
				
				c064SampleDelta_post[j] = buffer[i];
				j++;
				if(j==c064SampleDelta_post_length)
				{
					readC064SampleDelta_post = false;
					
					j=0;
					int[] c064SampleDelta_combined = new int[c064SampleDelta_pre.length+c064SampleDelta_post.length];
					System.arraycopy(c064SampleDelta_pre, 0, c064SampleDelta_combined, 0, c064SampleDelta_pre.length);
					System.arraycopy(c064SampleDelta_post, 0, c064SampleDelta_combined, c064SampleDelta_pre.length, c064SampleDelta_post.length);

					sample = GetDifference.getSampleDeltas(c064SampleDelta_combined, median, quantization, unompressedEEG, c0LengthInBits);
					
					if(sample.length>0)
                    {
                        results_current[0] = sample;
                        System.arraycopy(results_current, 0, results_new, packetNum, results_current.length);

                        packetNum++;
                        sample = new int[4][16];
                    }
					
					lookingForC0 = true;
					readC064SampleDelta = false;
					sampleDeltaIsEnough = false;
					countC0++;
					if(countC0==8)
					{
						//then starts looking for next E0
						lookingForC0 = false;
						found_0xE0 = false; // 8 0xC0 packets complete parsing, enable looking for 0xE0 (heart beat packet)
						countC0 = 0; //reset, accept new 8 0xC0 packets
						p++;
//						System.out.println("8 0xCO packet parsed " + p);
					}
					
					//debug
//					System.out.println("<<Position 7.1>>   j is = "+j+" i is = "+i+"  buffer["+i+"]="+buffer[i]);
					//end debug
				}
			}
//			System.out.print(buffer[i]+" ");
			
			
		}
		
		results_old = new int[packetNum][4][16];
        System.arraycopy(results_new, 0, results_old, 0, packetNum);
        packetNum=0;
        return results_old;
		
	}
	

	
	public static void main(String[] args) {

	
		MuseComm_ReadContinue.museBTInitial();
//		MuseComm_ReadContinue.museBTDataReceiver();
		
	}

}







