package com.gamemanager

class CommunicatingWithServerException : Exception {

	constructor(message: String, ex: Exception): super(message, ex)

	constructor(message: String): super(message)

	constructor(ex: Exception): super(ex)

}