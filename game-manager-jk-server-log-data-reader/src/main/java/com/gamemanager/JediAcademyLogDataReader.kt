package com.gamemanager

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.File
import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds.*
import kotlin.streams.toList


class JediAcademyLogDataReader(val file: File) {

	fun onChat() : Chat {
		return Chat(file)
	}

	class Chat(val file: File) {

		suspend fun watch(reader: (ChatMessage) -> Unit) = coroutineScope {
			async {
				try {
					// Creates a instance of WatchService.
					val watcher = FileSystems.getDefault().newWatchService()

					var lines = file.readLines();
					var lastIndex = lines.size

					// Registers the logDir below with a watch service.
					val directory = Paths.get(file.parent)
					directory.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE)

					// Monitor the logDir at listen for change notification.
					while (true) {

						val key = watcher.take()

						for (event in key.pollEvents()) {

							val kind = event.kind()

							if (ENTRY_CREATE == kind) {
								//println("Entry was created on log dir.")
							} else if (ENTRY_MODIFY == kind) {

								lines = file.readLines()

								val newLines = lines.subList(lastIndex, lines.size);

								lastIndex = lines.size

								val chatMessages = getChatMessages(newLines)

								chatMessages.forEach(reader)

							} else if (ENTRY_DELETE == kind) {
								//println("Entry was deleted from log dir.")
							}
						}
						key.reset()
					}
				} catch (e: IOException) {
					e.printStackTrace()
				} catch (e: InterruptedException) {
					e.printStackTrace()
				}
			}
		}

		fun readAllMessages(): List<ChatMessage> {

			val lines = file.readLines()

			return getChatMessages(lines)
		}

		private fun getChatMessages(lines: List<String>): List<ChatMessage> {
			return lines.stream()
					.filter { it.contains(Regex(": say: ")) }
					.map { toChatMessage(removeColorTags(it)) }
					.toList()
		}

		fun removeColorTags(str: String): String = str.replace(Regex("\\^+[0-9]"), "")

		fun toChatMessage(str: String) : ChatMessage {

			val firstSeparator = str.indexOf(":")
			val secondSeparator = str.indexOf(":", firstSeparator + 1)
			val thirdSeparator = str.indexOf(":", secondSeparator + 1)
			val fourthSeparator = str.indexOf(":", thirdSeparator + 1)

			val playerName = str.subSequence(thirdSeparator + 2, fourthSeparator).toString()

			val message = str.subSequence(fourthSeparator + 2, str.length).replace(Regex("\""), "")

			return ChatMessage(playerName, message)
		}

		data class ChatMessage(val playerName: String, val message: String)
	}

}

//suspend fun main() {
//	val file = File("server.log")
//
//	JediAcademyLogDataReader(file)
//			.onChat()
//			.watch { println("${it.playerName}: ${it.message}") }
//			.await()
//
//	//readAllMessages.forEach{println(it)}
//}