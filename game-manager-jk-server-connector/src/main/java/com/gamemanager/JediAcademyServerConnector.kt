package com.gamemanager

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

open class JediAcademyServerConnector(
		val inetAddress: InetAddress,
		val port: Int
) {
	constructor(host: String, port: Int) : this(InetAddress.getByName(host), port)

	private val OBB = 0xff.toByte()
	private val TIMEOUT_MILLISECONDS = 10000 // 10 sec
	private val MAX_PACKET_SIZE = 65507
	private val OBB_HEADER_PLACEHOLDER = "xxxx"

	@Throws(IOException::class)
	fun executeCommand(input: String): ByteArray {
		return doRequest(formatInput(input))
	}

	@Throws(IOException::class)
	private fun doRequest(inputBytes: ByteArray): ByteArray {
		val outputBytes = ByteArray(MAX_PACKET_SIZE)

		DatagramSocket().use {

			it.soTimeout = TIMEOUT_MILLISECONDS

			val packetToSend = DatagramPacket(inputBytes, inputBytes.size, inetAddress, port)
			it.send(packetToSend)

			val packetToReceive = DatagramPacket(outputBytes, outputBytes.size)
			it.receive(packetToReceive)
		}

		return outputBytes
	}

	private fun insertObbHeader(inputBytes: ByteArray): ByteArray {

		inputBytes[0] = OBB
		inputBytes[1] = OBB
		inputBytes[2] = OBB
		inputBytes[3] = OBB

		return inputBytes
	}

	private fun formatInput(input: String): ByteArray {
		return insertObbHeader((OBB_HEADER_PLACEHOLDER + input).toByteArray())
	}


}