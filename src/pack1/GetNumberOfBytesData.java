/*
 * 	This Method get the length of EEG data from compressed EEG package in bytes
 * 
 * 	Method should return the number of bytes (dataBytesCount)
 */
package pack1;

public class GetNumberOfBytesData {

	public static void main(String[] args) {

		int[] a = {1, 42};
		System.out.println(getNumberOfBitsData(a));
		System.out.println(getNumberOfBytesData(a));
		
	}
	
	public static int getNumberOfBitsData(int[] a) {
		
		@SuppressWarnings("unused")
		int dataBytesCount = 0;
		int dataBytes = a[0]*256+a[1];
//		dataBytesCount = dataBytes/8;
//		if((dataBytes%8)>0)
//			dataBytesCount++;
				
//		System.out.println(dataBytesCount);
		return dataBytes;	
	}
	public static int getNumberOfBytesData(int[] a) {

		@SuppressWarnings("unused")
		int dataBytesCount = 0;
		int dataBytes = a[0]*256+a[1];
		
			dataBytesCount = dataBytes/8;
				if((dataBytes%8)>0)
					dataBytesCount++;
		
		//		System.out.println(dataBytesCount);
		return dataBytesCount;	
	}

}
