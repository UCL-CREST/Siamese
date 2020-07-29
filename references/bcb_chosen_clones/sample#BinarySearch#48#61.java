	public static <T extends Comparable<T>> int binarySearch4(T[] arr, T key) {
		int imin = 0;
		int imax = arr.length - 1;
		while(imin <= imax) {
			int imid = imin + (imax - imin)/2;
			if (key.compareTo(arr[imid]) < 0)//(key < arr[imid])
				imax = imid-1;
			else if (key.compareTo(arr[imid]) > 0)//(key > arr[imid])
				imin = imid + 1;
			else
				return imid;
		}
		return -1;
	}
