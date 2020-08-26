package com.gamemanager

import com.gamemanager.ErrorMessages.ERROR_COMMUNICATING_WITH_THE_SERVER
import com.gamemanager.JediAcademyCommandString.INFO_COMMAND
import com.gamemanager.JediAcademyCommandString.STATUS_COMMAND

open class JediAcademyServerManager(
		val connector: JediAcademyServerConnector) {

	fun asAnonymous(): AnonymousCommandSender {
		return AnonymousCommandSender(connector)
	}

	fun asSmod(pass: String): SmodCommandSender {
		return SmodCommandSender(connector, pass)
	}

	fun asRoot(pass: String): RootCommandSender {
		return RootCommandSender(connector, pass)
	}

	open class AnonymousCommandSender(val connector: JediAcademyServerConnector) {

		@Throws(CommunicatingWithServerException::class)
		private fun <T>doCommand(command: JediAcademyCommandString, translator: (ByteArray) -> T) : T {
			try {
				return translator(connector.executeCommand(command.value))
			} catch (e: Exception) {
				throw CommunicatingWithServerException(ERROR_COMMUNICATING_WITH_THE_SERVER, e)
			}
		}

		fun basicInfo() : Map<ServerStatusType, String> = doCommand(INFO_COMMAND, ServerStatusTranslator.Companion::translate)

		fun detailedStatus(): Map<ServerStatusType, String> = doCommand(STATUS_COMMAND, ServerStatusTranslator.Companion::translate)

		fun getPlayerCount(): Int = (basicInfo()[ServerStatusType.CLIENTS] ?: "0").toInt()
	}

	open class SmodCommandSender(connector: JediAcademyServerConnector, val pass: String) : AnonymousCommandSender(connector) {
		fun changeMap(mapName: String?) {
			//TODO
		}

	}

	class RootCommandSender(connector: JediAcademyServerConnector, pass: String) : SmodCommandSender(connector, pass) {
		fun resetServer() {
			//TODO
		}
	}

}