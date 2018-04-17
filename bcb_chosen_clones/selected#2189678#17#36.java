    public static void binarySearch(int[] array, int lowerbound, int upperbound, int key) {
        int position;
        int comparisonCount = 1;
        position = (lowerbound + upperbound) / 2;
        while ((array[position] != key) && (lowerbound <= upperbound)) {
            comparisonCount++;
            if (array[position] > key) {
                upperbound = position - 1;
            } else {
                lowerbound = position + 1;
            }
            position = (lowerbound + upperbound) / 2;
        }
        if (lowerbound <= upperbound) {
            System.out.println("The number was found in array subscript" + position);
            System.out.println("The binary search found the number after " + comparisonCount + "comparisons.");
        } else {
            System.out.println("Sorry, the number is not in this array.  The binary search made " + comparisonCount + " comparisons.");
        }
    }
