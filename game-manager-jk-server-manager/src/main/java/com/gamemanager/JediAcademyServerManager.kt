package com.gamemanager

import com.gamemanager.ErrorMessages.ERROR_COMMUNICATING_WITH_THE_SERVER

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

	abstract class AbstractCommandSender(val connector: JediAcademyServerConnector) {

		fun <T>executeCommand(command: JediAcademyCommand<T>) : T {
			return command.runCatching { execute(connector::doRequest) }
					.getOrElse { throw CommunicatingWithServerException(ERROR_COMMUNICATING_WITH_THE_SERVER, it) }
		}

	}

	class AnonymousCommandSender(connector: JediAcademyServerConnector) : AbstractCommandSender(connector) {

		fun getBasicInfo() = executeCommand(JediAcademyCommand.Info())

		fun getDetailedStatus() = executeCommand(JediAcademyCommand.Status())

		fun getPlayerCount() = (getBasicInfo()[ServerStatusType.CLIENTS] ?: "0").toInt()

		fun isServerAvailable()  = executeCommand(JediAcademyCommand.Connect())

		fun getChallengeNumber()  = executeCommand(JediAcademyCommand.Challenge())
	}

	open class SmodCommandSender(connector: JediAcademyServerConnector, val pass: String) : AbstractCommandSender(connector) {
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