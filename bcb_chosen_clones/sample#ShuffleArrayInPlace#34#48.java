	public static void shuffle2(int[] a) {
		//Alternate Fisher-Yates/Knuth Shuffle
		Random random = new Random();
		random.nextInt();
		
		for(int i = a.length-1; i >= 1; i--) {
			//Choose index to swap from 0 <= j <= i
			int j = random.nextInt(i+1);
			
			//Swap
			int tmp = a[i];
			a[i] = a[j];
			a[j] = tmp;
		}
	}
