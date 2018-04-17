	public static <T extends Comparable<T>> void BubbleSortComparable2(T[] num) {
		int last_exchange;
		int right_border = num.length - 1;
		do {
			last_exchange = 0;
			for (int j = 0; j < num.length - 1; j++) {
				if (num[j].compareTo(num[j + 1]) > 0)
				{
					T temp = num[j];
					num[j] = num[j + 1];
					num[j + 1] = temp;
					last_exchange = j;
				}
			}
			right_border = last_exchange;
		} while (right_border > 0);
	}
