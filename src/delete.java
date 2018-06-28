
public class delete {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] buffer = {1,2,3,4,5,6,7,8,9,10};
		int[] second = new int[buffer.length-6];
		for(int i=0;i<buffer.length;i++)
		{
			if(i==6)
			{
				System.out.println("buffer.length = "+ buffer.length);
				System.out.println("current i = "+i);
				System.out.println("current buffer value = "+ buffer[i]);
			}
		}
		
	}

}
