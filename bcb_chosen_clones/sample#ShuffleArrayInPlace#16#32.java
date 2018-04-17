	public static void shuffle1(int[] a) {
		//Standard Fisher-Yates/Knuth Shuffle
		int length = a.length;
		
		Random random = new Random();
		random.nextInt();
		
		for(int i = 0; i < length; i++) {
			//Chose index to swap with from i <= j < length
			int j = i + random.nextInt(length-i);
			
			//Swap
			int tmp = a[i];
			a[i] = a[j];
			a[j] = tmp;
		}
	}
