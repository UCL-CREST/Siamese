	public static int binarySearch2(int arr[], int key) {
		//Implementation: Iterative, primitive type.
		int imin = 0;
		int imax = arr.length - 1;
		while(imin <= imax) {
			int imid = imin + (imax - imin)/2;
			if (key < arr[imid])
				imax = imid-1;
			else if (key > arr[imid])
				imin = imid + 1;
			else
				return imid;
		}
		return -1;
	}
