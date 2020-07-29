	public static <T extends Comparable<T>> void BubbleSortComparable1(T[] num) {
		int j;
		boolean flag = true; // set flag to true to begin first pass
		T temp; // holding variable

		while (flag) {
			flag = false; // set flag to false awaiting a possible swap
			for (j = 0; j < num.length - 1; j++) {
				if (num[j].compareTo(num[j + 1]) > 0) // change to > for ascending sort
				{
					temp = num[j]; // swap elements
					num[j] = num[j + 1];
					num[j + 1] = temp;
					flag = true; // shows a swap occurred
				}
			}
		}
	}
