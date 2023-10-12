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


public class FileServer
{
	public static void main(String[] args)
	{
		int nPort = Integer.parseInt(args[0]);
		System.out.println("Server: Listening on port " + args[0] + "...");
		ServerSocket serverSocket;
		Socket serverEndpoint;

		try 
		{
			serverSocket = new ServerSocket(nPort);
			serverEndpoint = serverSocket.accept();
			
			System.out.println("Server: New client connected: " + serverEndpoint.getRemoteSocketAddress());
			
			DataInputStream disReader = new DataInputStream(serverEndpoint.getInputStream());
			System.out.println(disReader.readUTF());
			
			DataOutputStream dosWriter = new DataOutputStream(serverEndpoint.getOutputStream());
			dosWriter.writeUTF("Server: Hello World!");
			writeFile("Download.txt");
			sendFile("Download.txt", dosWriter);

			serverEndpoint.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			System.out.println("Server: Connection is terminated.");
		}
	}

	/*
	 * Codes to create a new file and write to it
	 */
	public static void writeFile(String sFileName) {
		try {
			FileWriter fwWriter = new FileWriter(sFileName);
			fwWriter.write("Hello World!");
			fwWriter.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Codes to send the file to the client
	 */
	public static void sendFile(String path, DataOutputStream dosWriter) throws Exception {
		int bytes = 0;
		
		File file = new File(path);
		FileInputStream fisReader = new FileInputStream(file);
		
		dosWriter.writeLong(file.length());

		byte[] buffer = new byte[4096];

		System.out.println("Server: Sending file \"" + path + "\"" + " (" + file.length() + " bytes)");

		while ((bytes = fisReader.read(buffer)) != -1) {
			dosWriter.write(buffer, 0, bytes);
			dosWriter.flush();
		}

		// Close the streams
		fisReader.close();
	}
}