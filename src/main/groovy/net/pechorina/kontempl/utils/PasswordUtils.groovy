package net.pechorina.kontempl.utils

class PasswordUtils {
	
	static String getAlphaNum(int size) {
		def alphabet = (('A'..'Z') + ('0'..'9')).join()
		String p = generator(alphabet, size)
		return p
	}
	
	static String getAlpha(int size) {
		def alphabet = ('A'..'Z').join()
		String p = generator(alphabet, size)
		return p
	}
	
	def generator = { String alphabet, int n ->
		new Random().with {
		  (1..n).collect { alphabet[ nextInt( alphabet.length() ) ] }.join()
		}
	}
}
