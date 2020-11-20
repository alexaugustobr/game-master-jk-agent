package com.gamemanager

enum class JediAcademyCommandType(val value: String) {
	/* Anonymous */
	INFO("getinfo"),
	STATUS("getstatus"),
	CONNECT("connect"),
	CHALLENGE("getchallenge");

}

sealed class JediAcademyCommand<T>(private val type: JediAcademyCommandType,
                                   private val translate: (ByteArray) -> T) {

	fun execute(requestFunction: (String) -> ByteArray) : T {
		return translate(requestFunction(type.value))
	}

	class Info() : JediAcademyCommand<Map<ServerStatusType, String>>(
			JediAcademyCommandType.INFO, StatusResponseTranslator.Companion::translate
	)

	class Status() : JediAcademyCommand<Map<ServerStatusType, String>>(
			JediAcademyCommandType.STATUS, StatusResponseTranslator.Companion::translate
	)

	class Connect() : JediAcademyCommand<Boolean>(
			JediAcademyCommandType.CONNECT, { String(it).contains("Server uses protocol version", true) }
	)

	class Challenge() : JediAcademyCommand<Int>(
			JediAcademyCommandType.CHALLENGE, { String(it).substring(22).toInt() }
	)

}


//fun main(args: Array<String>) {
//
//	val a = "����challengeResponse -405009004"
//
//	val n = a.substring(22).toInt()
//
//	println(n)
//}