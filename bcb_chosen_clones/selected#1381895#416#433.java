    private static int binarySearchValue(final int value, long[] longArray, final int trueLength, final boolean searchByPosition) {
        int lowerIndex = 0;
        int upperIndex = trueLength - 1;
        int midIndex = 0;
        int valueAtMidIndex;
        while (lowerIndex <= upperIndex) {
            midIndex = (lowerIndex + upperIndex) / 2;
            valueAtMidIndex = searchByPosition ? extractPositionFromCompactForm(longArray[midIndex], false) : extractRsIdFromCompactForm(longArray[midIndex], true);
            if (value == valueAtMidIndex) {
                return midIndex;
            } else if (value > valueAtMidIndex) {
                lowerIndex = midIndex + 1;
            } else {
                upperIndex = midIndex - 1;
            }
        }
        return -1;
    }
