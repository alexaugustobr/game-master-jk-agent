package com.gamemanager;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

@AllArgsConstructor
@Getter
public class JediAcademyServerConnector {
	
	private final InetAddress inetAddress;
	private final int port;
	private static final byte OBB = (byte) 0xff;
	
	private static final int TIMEOUT_MILLISECONDS = 3000; // 10 sec
	private static final int MAX_PACKET_SIZE = 65507;
	private static final String OBB_HEADER_PLACEHOLDER = "xxxx";
	
	public JediAcademyServerConnector(String host,
									int port) throws UnknownHostException {
		this.inetAddress = InetAddress.getByName(host);
		this.port = port;
	}
	
	public byte[] executeCommand(String input) throws IOException {
		return doRequest(formatInput(input));
	}
	
	private byte[] doRequest(byte[] inputBytes) throws IOException {
		final byte[] outputBytes = new byte[MAX_PACKET_SIZE];
		
		try (DatagramSocket socket = new DatagramSocket()) {
			socket.setSoTimeout(TIMEOUT_MILLISECONDS);
			
			final DatagramPacket packetToSend = new DatagramPacket(inputBytes, inputBytes.length, inetAddress, port);
			socket.send(packetToSend);
			
			final DatagramPacket packetToReceive = new DatagramPacket(outputBytes, outputBytes.length);
			socket.receive(packetToReceive);
		}
		
		return outputBytes;
	}
	
	private byte[] insertObbHeader(byte[] inputBytes) {
		inputBytes[0] = OBB;
		inputBytes[1] = OBB;
		inputBytes[2] = OBB;
		inputBytes[3] = OBB;
		
		return inputBytes;
	}
	
	private byte[] formatInput(String input) {
		return insertObbHeader((OBB_HEADER_PLACEHOLDER + input).getBytes());
	}
	
}
