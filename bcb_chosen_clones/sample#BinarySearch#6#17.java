	public static int binarySearch1(int arr[], int key, int imin, int imax) {
		//Implementation: Recursive, primitive type
		if(imax < imin)
			return -1;
		int imid = (imin+imax)/2;
		if(arr[imid] > key)
			return binarySearch1(arr,key,imin,imid-1);
		else if (arr[imid] < key)
			return binarySearch1(arr,key,imid+1,imax);
		else
			return imid;		
	}
