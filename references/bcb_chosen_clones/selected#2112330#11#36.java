    public static int binarySearch(int[] array, int value) {
        int low = 0;
        int high = array.length - 1;
        int middle;
        while (low <= high) {
            middle = (low + high) / 2;
            for (int i = 0; i < array.length; i++) {
                System.out.print(array[i]);
                if (i == middle) {
                    System.out.print("#");
                }
                System.out.print(" ");
            }
            System.out.println();
            if (array[middle] == value) {
                return middle;
            }
            if (value < array[middle]) {
                high = middle - 1;
            }
            if (value > array[middle]) {
                low = middle + 1;
            }
        }
        return -1;
    }
