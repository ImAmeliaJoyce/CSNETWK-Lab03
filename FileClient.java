/*
 * CSNETWK - S16
 * GROUP 4
 * 
 * ABENOJA, Amelia Joyce L.
 * SANG, Nathan Immanuel C.
 * 
 * References:
 * - For understanding the file transfer between the client and the server
 * 		https://www.geeksforgeeks.org/transfer-the-file-client-socket-to-server-socket-in-java/?fbclid=IwAR33tlfZbFbKd2cWNEjEyAV3qKjGWnOvKJ524j0DmvpK_cG_7qpb2x7Nzd4
 */


import java.net.*;
import java.io.*;


public class FileClient
{
	public static void main(String[] args)
	{
		String sServerAddress = args[0];
		int nPort = Integer.parseInt(args[1]);
		
		try
		{
			Socket clientEndpoint = new Socket(sServerAddress, nPort);
			System.out.println("Client: Connecting to server at" + clientEndpoint.getRemoteSocketAddress());
			
			System.out.println("Client: Connected to server at" + clientEndpoint.getRemoteSocketAddress());
			
			DataOutputStream dosWriter = new DataOutputStream(clientEndpoint.getOutputStream());
			dosWriter.writeUTF("Client: Hello from client" + clientEndpoint.getLocalSocketAddress());
			
			DataInputStream disReader = new DataInputStream(clientEndpoint.getInputStream());
			System.out.println(disReader.readUTF());
			receiveFile("Received.txt", disReader);
			
			clientEndpoint.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("Client: Connection is terminated.");
		}
	}

	/*
	 * Codes to receive a file from the server and save it to the client's machine as "Received.txt"
	 */
	public static void receiveFile(String sFileName, DataInputStream disReader) throws IOException {
		int bytes = 0;
		
		FileOutputStream fosWriter = new FileOutputStream(sFileName);
		byte[] buffer = new byte[4096]; // (4*1024)

		// Read file size first
		long filesize = disReader.readLong();

		while (filesize > 0 && (bytes = disReader.read(buffer, 0, (int)Math.min(buffer.length, filesize))) != -1) {
			fosWriter.write(buffer, 0, bytes);
			filesize -= bytes;
		}

		System.out.println("Client: Downloaded file \"Received.txt\"");
		fosWriter.close();
	}
}