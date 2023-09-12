
package com.megpbr.audit;

import java.util.*;
import java.io.*;

public class CaptchaCheck {

	// Returns true if given two strings are same
	public static boolean checkCaptcha(String captcha, String user_captcha) {
		return captcha.equals(user_captcha);
	}

	// Generates a CAPTCHA of given length
	public static String generateCaptcha(int n) {
		// to generate random integers in the range [0-61]
		Random rand = new Random(62);

		// Characters to be included
		String chrs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

		// Generate n characters from above set and
		// add these characters to captcha.
		String captcha = "";
		while (n-- > 0) {
			int index = (int) (Math.random() * 62);
			captcha += chrs.charAt(index);
		}

		return captcha;
	}

	// Driver code
	public static void main(String[] args) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		// Generate a random CAPTCHA
		String captcha = generateCaptcha(9);
		System.out.println(captcha);

		// Ask user to enter a CAPTCHA
		System.out.println("Enter above CAPTCHA: ");
		String usr_captcha = reader.readLine();

		// Notify user about matching status
		if (checkCaptcha(captcha, usr_captcha))
			System.out.println("CAPTCHA Matched");
		else
			System.out.println("CAPTCHA Not Matched");
	}
}