package pack2;

public class GetNextBit {

	public static void main(String[] args) {
		/*
		 * 	one byte come in to this class
		 */
//		int a = 225;
		for (int i = 0;i<8;i++)
		System.out.print((int)getNextBitFromByte(170, i));
	

	}
	public static char getNextBitFromByte(int a, int bitPosition)
	{
		char c = (char) a;
		char mask = (char) (0x80);
//		System.out.println("£££: "+(int)c);
		int  bitValue = (char) (c&(mask>>bitPosition));
		char d = 0;
//		System.out.println("£££: "+(int)bitValue);
		for(int i=0;i<8;i++)
		{
			if(bitValue==(1<<i))
				d = 1;
			if(bitValue == 0)
				d = 0;
		}
//		else
//			System.out.println("Error in GetNextBitFromByte");
		return d;
	}

}
