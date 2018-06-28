import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import jssc.SerialPort;
import jssc.SerialPortException;

public class MuseComm_ReadContinue {

	private static SerialPort serialPort = new SerialPort("/dev/tty.Muse-RN-iAP");

	private static final int bufferSize = 200;
	private static int[][][] EEG;
	//Muse serial communication commands
	private static final byte[] startStreaming = {0x73, 0x0A}; //s <LF>
	private static final byte[] versionHandshake = {0x76, 0x20, 0x32, 0x0A}; // v 2 <LF>
	private static final byte[] loadPreset10 = {0x25, 0x20, 0x31, 0x30, 0x0A}; //% 10 <LF>
	private static final byte[] loadPreset14 = {0x25, 0x20, 0x31, 0x34, 0x0A}; //% 14 <LF>
	private static final byte[] showStatus = {0x3F, 0x0A}; // ? <LF>
	private static final byte[] keepAlive = {0x6B, 0x0A}; //k <LF>
	private static final byte[] chooseNotchFilter = {0x67, 0x20, 0x34, 0x30, 0x38, 0x43, 0x0A}; //g 408C <LF>   'g' = Choose Notch Frequency
    /*
     * add more serial command see 
     * https://sites.google.com/a/interaxon.ca/muse-developer-site/muse-communication-protocol/serial-commands
     */
	
	public static void museBTInitial() {
		try {
			serialPort.openPort();//Open serial port
			//Set params. Also you can set params by this string: serialPort.setParams(9600, 8, 1, 0);
			if(serialPort.isOpened())
			{
				serialPort.setParams(SerialPort.BAUDRATE_9600, 
						SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				myDelayMethod(200);
				serialPort.writeBytes(versionHandshake);//Write data to port
				serialPort.writeBytes(loadPreset10);
				serialPort.writeBytes(chooseNotchFilter);
				serialPort.writeBytes(startStreaming);
				myDelayMethod(200);
				
				museBTDataReceiver();
			}
			else
			{
				System.out.println("PORT CANT OPEN !! ");
				serialPort.closePort();
			}
        }
        catch (SerialPortException ex) { System.out.println(ex); }
	}

	public static void museBTDataReceiver() {
		int[] buffer = new int[bufferSize];
		while(true) // Future enhensment: until user stop this loop will keep going
		{			
			
			try {
				buffer = serialPort.readIntArray(bufferSize);
				
				
					
//				for(int i=0;i<900;i++)
//					System.out.print(buffer[i]+" ");
				EEG = MuseComm_Parser.museBTDataReceiver(buffer);
//				System.out.print("one buffer size parsed ...");
				serialPort.writeBytes(keepAlive);
				
			} 
			catch (SerialPortException e) {e.printStackTrace();}


		}
//			serialPort.closePort();//Close serial port
		
		
	}
	
	private static void myDelayMethod(int t) {	
		try {
			Thread.sleep(t);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {

		museBTInitial();
		
	}

}
