package main;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Client implements Runnable {
	private static Client instance = null;
	private static Thread clientThread = null;
	private static final String FILE_ACCESS = "r";
	private static final int BUFFER_SIZE = 64 * 1024;
	private static final int MEMORY_MAPPED_BUFFER = 64 * 1024 * 1024;
	private static File file = null;

	int port;
	String address = null;
	Socket socket;

	public static synchronized Client getInstance() {
		if (instance == null) {
			instance = new Client();
		}
		return instance;
	}

	public void start(File file) {
		Client.file = file;
		clientThread = new Thread(instance);
		Window.log(" server started \n");
		clientThread.start();
	}

	public boolean createSocket(String address, String portStr) {
		InetAddress ipAddress = null;
		int port = Integer.parseInt(portStr);
		try {
			ipAddress = InetAddress.getByName(address);
			socket = new Socket(ipAddress, port);
			return true;
		} catch (UnknownHostException e) {
			Window.log("unknown host \n");
			e.printStackTrace();
		} catch (IOException e) {
			Window.log("IO exception \n");
			e.printStackTrace();
		}
		return false;
	}

	public void sendFile() {
		DataOutputStream outD;
		MappedByteBuffer bufferInRAM;
		try {
			Window.blockButton();
			outD = new DataOutputStream(socket.getOutputStream());
			RandomAccessFile RAfile = new RandomAccessFile(file, FILE_ACCESS);

			outD.writeLong(RAfile.length());// מעסûכאול נאחלונ פאיכא
			outD.writeUTF(file.getName());// מעסûכאול טלÿ פאיכא
			FileChannel fileChannel = RAfile.getChannel();
			long sended = 0;
			while(file.length()>sended){
			long bufferSize = ((fileChannel.size()-sended)>MEMORY_MAPPED_BUFFER) ? MEMORY_MAPPED_BUFFER: fileChannel.size()-sended; 
			bufferInRAM =fileChannel.map(FileChannel.MapMode.READ_ONLY, sended,bufferSize );

			byte[] sendBuffer = new byte[BUFFER_SIZE];

			while (bufferInRAM.remaining() > sendBuffer.length) {
				Window.setText(sended + "/" + RAfile.length()+ "\n");
				bufferInRAM.get(sendBuffer, 0, sendBuffer.length);
				outD.write(sendBuffer, 0, sendBuffer.length);
			}
			sendBuffer = new byte[bufferInRAM.remaining()];
			bufferInRAM.get(sendBuffer, 0, sendBuffer.length);
			outD.write(sendBuffer, 0, sendBuffer.length);
			Window.setText(sended + "/" + RAfile.length() + "\n");
			sended+=bufferSize;
			bufferInRAM.clear(); 
			
			}
			Window.setText(sended + "/" + RAfile.length() + "\n");
			Window.log("Done");
			outD.flush();
			fileChannel.close();
			RAfile.close();
			socket.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		Window.unblockButton();
	}

	@Override
	public void run() {
		sendFile();

	}
}
