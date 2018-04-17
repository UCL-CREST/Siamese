    protected static int binarySearch(final int values[], int start, int end, int target) {
        if (DEBUG_IDS) {
            System.out.println("binarySearch(), target: " + target);
        }
        while (start <= end) {
            int middle = (start + end) / 2;
            int value = values[middle];
            if (DEBUG_IDS) {
                System.out.print("  value: " + value + ", target: " + target + " // ");
                print(values, start, end, middle, target);
            }
            if (value == target) {
                while (middle > 0 && values[middle - 1] == target) {
                    middle--;
                }
                if (DEBUG_IDS) {
                    System.out.println("FOUND AT " + middle);
                }
                return middle;
            }
            if (value > target) {
                end = middle - 1;
            } else {
                start = middle + 1;
            }
        }
        if (DEBUG_IDS) {
            System.out.println("NOT FOUND!");
        }
        return -1;
    }
