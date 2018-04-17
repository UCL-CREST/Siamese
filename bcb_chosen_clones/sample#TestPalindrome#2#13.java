	public static boolean isPalindrome(String original) {
		//A not very efficient example
		String reverse = "";
		int length = original.length();
		for (int i = length - 1; i >= 0; i--)
			reverse = reverse + original.charAt(i);

		if (original.equals(reverse))
			return true;
		else
			return false;
	}
