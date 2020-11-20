package com.gamemanager

import java.util.*

class StatusResponseTranslator {

	companion object {

		private val VALID_MESSAGE_HEADERS = arrayOf("infoResponse", "statusResponse")
		private val REPLACEMENT_PLACE_HOLDER = "#SPLIT#"

		@Throws(UnreadableMessageException::class)
		@JvmStatic
		fun translate(byteMessage: ByteArray?): Map<ServerStatusType, String> {
			if (byteMessage == null || byteMessage.size == 0) {
				throw UnreadableMessageException(ErrorMessages.ERROR_MESSAGE_IS_EMPTY)
			}
			val statusMessage = String(byteMessage)
			if (!containsAnyValidHeader(statusMessage)) {
				throw UnreadableMessageException(ErrorMessages.ERROR_MESSAGE_HAS_NO_VALID_HEADER)
			}
			val messageBody = extractMessageBody(statusMessage)
			val keyValuesList = Arrays.asList(*messageBody.split(REPLACEMENT_PLACE_HOLDER).toTypedArray())
			return transformIntoMap(keyValuesList)
		}

		private fun transformIntoMap(keyValuesList: List<String>): Map<ServerStatusType, String> {
			val map: MutableMap<ServerStatusType, String> = EnumMap(ServerStatusType::class.java)
			val iterator = keyValuesList.iterator()
			while (iterator.hasNext()) {
				val key = iterator.next()
				var value: String? = null
				if (iterator.hasNext()) {
					value = iterator.next()
				}
				if (value != null) {
					map[ServerStatusType.getEnumValueByKey(key).orElse(ServerStatusType.UNKNOWN)] = value
				}
			}
			return map
		}

		/*
		 * Removes the message header
		 */
		private fun extractMessageBody(statusMessage: String): String {
			//+2 to ignore the first \ char in order to split correctly
			val begin = statusMessage.indexOf("\n") + 2
			return statusMessage.substring(begin)
					.replace("\\", REPLACEMENT_PLACE_HOLDER)
		}

		private fun containsAnyValidHeader(statusMessage: String): Boolean {
			return Arrays.stream(VALID_MESSAGE_HEADERS).anyMatch { s: String? -> statusMessage.contains(s!!) }
		}

	}

}