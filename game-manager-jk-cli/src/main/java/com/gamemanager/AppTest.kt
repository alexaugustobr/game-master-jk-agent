package com.gamemanager

import java.net.InetAddress
import java.util.logging.Logger.getLogger

class AppTest() {

	private val log = getLogger(AppTest::class.java.toString())

	init {
		val manager = JediAcademyServerManager(
				JediAcademyServerConnector(InetAddress.getByName("45.137.149.2"), 29070)
		)

		println(manager.asAnonymous().isServerAvailable())

		manager.asAnonymous().run {
			println(getBasicInfo())
		}

		val anonymousCommandSender = manager.asAnonymous()

		val playerCount = anonymousCommandSender.getPlayerCount()
	}
}

fun main(args: Array<String>) {
	AppTest()
}