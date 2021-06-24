package com.miltos.research.test;

import java.util.Base64;

public class Base64Test {

	public static void main(String[] args) {

		// 0. Original Message
		String origMessage = "username:password";
				
		// 1. Encode the message
		byte[] encodedBytes = Base64.getEncoder().encode(origMessage.getBytes());
		System.out.println("encodedBytes " + new String(encodedBytes));

		// 3. Decode the Message
		byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
		System.out.println("decodedBytes " + new String(decodedBytes));
	}

}
