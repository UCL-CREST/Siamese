    private static int binarySearchSampleIndex(short sampleIndex, int[] intArray, int trueLength) {
        int lowerIndex = 0;
        int upperIndex = trueLength - 1;
        int midIndex = 0;
        short valueAtMidIndex;
        while (lowerIndex <= upperIndex) {
            midIndex = (lowerIndex + upperIndex) / 2;
            valueAtMidIndex = extractSampleIndexFromInteger(intArray[midIndex]);
            if (sampleIndex == valueAtMidIndex) {
                return midIndex;
            } else if (sampleIndex > valueAtMidIndex) {
                lowerIndex = midIndex + 1;
            } else {
                upperIndex = midIndex - 1;
            }
        }
        return -1;
    }
