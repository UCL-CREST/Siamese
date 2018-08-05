	public static int gcd3(int a, int b) {
		if (b == 0) {
			return 1;
		} else {
			return gcd3(b, a % b);
		}
	}
