package pack2;

public class Convert {

	public static void main(String[] args) {
//		 initalize received compressed sample data
//		int[] a = {8, 38, 150, 180, 80, 22, 150, 104, 83, 22, 80, 204, 
//				22, 243, 193, 216, 190, 86, 70, 97, 128, 22, 11, 122, 
//				217, 44, 95, 11, 31, 81, 192, 154, 233, 54, 101, 34, 
//				170, 194, 17, 138, 80};
//		int length = 39;
		
		int[] a = {190,245,38,8,100};
//		int length = 5;
		int[][] b = Convert.to2DBitArray(a, a.length);
		
//		for(int k=0;k<a.length;k++)
//		{
//			for(int i=0;i<8;i++)
//			{
//				System.out.print(b[k][i]);
//			}
//		}
		
//		int[][] c = Convert.reverse(b, a.length, 8);
//		
//		for(int k=0;k<a.length;k++)
//		{
//			for(int i=0;i<8;i++)
//			{
//				System.out.print(c[k][i]);
//			}
//		}
		
//		int[] test = {0,1};
//		System.out.print(Convert.toInt(test, test.length));
		
		int[] test = {1,2,3,4};
		int[] array = Convert.reverse(test, test.length);
		for(int i=0;i<test.length;i++)
			System.out.print(array[i]);
	}
	
	public static int[][] to2DBitArray(int[] a, int length) {
		int[][] bitArray = new int[length][8];
		for(int k=0;k<length;k++)
			for(int i=0;i<8;i++)
				bitArray[k][i] = GetNextBit.getNextBitFromByte(a[k], i);
		return bitArray;
	}
	public static int[][] reverse(int[][] bitArray, int length, int i) 
	{
		int[][] reversedBitArray = new int[length][8];
		for(int k=0;k<length;k++)
			for(i=0;i<8;i++)
			{
				reversedBitArray[k][i] = bitArray[length-k-1][8-i-1];
			}

		//		for(int k=0;k<a.length;k++)
		//			{
		//				for(int i=0;i<8;i++)
		//				{
		//					System.out.print(reversedBitArray[k][i]);
		//				}
		//			}
		return reversedBitArray;
	}
	public static int[] reverse(int[] bitArray, int length) 
	{
		int temp;
		for(int i=0;i<length/2;i++)
		{
			temp = bitArray[i];
			bitArray[i] = bitArray[length-1-i];
			bitArray[length-1-i] = temp;
		}
		return bitArray;
	}
	public static int toInt(int[] a, int length)
	{
		char val = 0;
		for(int i=0;i<length;i++)
		{
			val = (char) (val|(a[i]<<i));
		}
		return val;
		
	}

}
