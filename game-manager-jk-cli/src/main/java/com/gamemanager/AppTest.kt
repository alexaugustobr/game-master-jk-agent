package com.gamemanager

import java.net.InetAddress
import java.util.logging.Logger.getLogger

class AppTest() {

	private val log = getLogger(AppTest::class.java.toString())

	init {
		val manager = JediAcademyServerManager(JediAcademyServerConnector(InetAddress.getByName(
				"52.67.23.56"),
				29070)
		)

		val anonymousCommandSender = manager.asAnonymous()

		manager.asRoot("pass")
				.resetServer()

		manager.asSmod("pass")
				.changeMap("mapName")

		val playerCount = anonymousCommandSender.getPlayerCount()
	}
}

fun main(args: Array<String>) {
	AppTest()
}