package com.gamemanager

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.io.File
import java.io.IOException
import java.nio.file.FileSystems
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds.*
import java.util.*
import kotlin.streams.toList


class LogReader(val file: File) {

	fun onChat() : Chat {
		return Chat(file)
	}

	class Chat(val file: File) {

		suspend fun watchChanges(onChangeCallBack: (Message) -> Unit) = coroutineScope {
			async {
				try {
					val directory = Paths.get(file.parent)
					val watcher = FileSystems.getDefault().newWatchService()

					directory.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE)

					var lastIndex = getLastLineFromFile(file)

					while (true) {

						val key = watcher.take()

						for (event in key.pollEvents()) {
							when (event.kind()) {
								ENTRY_CREATE -> println("Entry was created on log dir.")
								ENTRY_DELETE -> println("Entry was deleted from log dir.")
								ENTRY_MODIFY -> {
									val lines = readLinesFromIndex(file, lastIndex)
									//update the last index
									lastIndex += lines.size
									createChatMessageFromLines(lines).forEach(onChangeCallBack)
								}
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

		private fun createChatMessageFromLines(lines: List<String>): List<Message> = lines.stream()
				.filter { it.contains(Regex(": say: ")) }
				.map { toChatMessage(removeColorTags(it)) }
				.toList()

		private fun removeColorTags(str: String): String = str.replace(Regex("\\^+[0-9]"), "")

		private fun toChatMessage(str: String) : Message {

			val firstSeparator = str.indexOf(":")
			val secondSeparator = str.indexOf(":", firstSeparator + 1)
			val thirdSeparator = str.indexOf(":", secondSeparator + 1)
			val fourthSeparator = str.indexOf(":", thirdSeparator + 1)

			val playerName = str.subSequence(thirdSeparator + 2, fourthSeparator).toString()

			val message = str.subSequence(fourthSeparator + 2, str.length).replace(Regex("\""), "")

			return Message(playerName, message)
		}

		private fun getLastLineFromFile(file: File) : Long {
			var lastLine = 0L
			val fileScanner = Scanner(file)

			while (fileScanner.hasNextLine()) {
				fileScanner.nextLine()
				lastLine++
			}

			return lastLine
		}

		private fun readLinesFromIndex(file: File, initalIndex: Long) : List<String> {
			var lastLine = 0L
			val fileScanner = Scanner(file)
			val lines = mutableListOf<String>()

			while (fileScanner.hasNextLine()) {
				if (lastLine >= initalIndex) {
					lines.add(fileScanner.nextLine())
				} else {
					fileScanner.nextLine()
				}
				lastLine++
			}

			return lines
		}

		private fun readAllMessages(): List<Message> = createChatMessageFromLines(file.readLines())

		data class Message(val playerName: String, val message: String)
	}

}

suspend fun main() {
	val file = File("/home/admin/server.log")

	LogReader(file).onChat()
			.watchChanges { println("${it.playerName}: ${it.message}") }
			.await()


}