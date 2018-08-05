	public static <T> void shuffle3(T[] a) {
		//Standard Fisher-Yates/Knuth Shuffle for Object array
		int length = a.length;
				
		Random random = new Random();
		random.nextInt();
				
		for(int i = 0; i < length; i++) {
			//Chose index to swap with from i <= j < length
			int j = i + random.nextInt(length-i);
			
			//Swap
			T tmp = a[i];
			a[i] = a[j];
			a[j] = tmp;
		}
	}
