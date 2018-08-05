    private static int assignBytecodeIndices(int bcIndex, int[] bcIndices, int low, int high) {
        int middle = (high + low) / 2;
        bcIndices[middle] = bcIndex++;
        if (low == middle && middle == high) {
            return bcIndex;
        } else {
            if (low < middle) {
                bcIndex = assignBytecodeIndices(bcIndex, bcIndices, low, middle - 1);
            }
            if (middle < high) {
                bcIndex = assignBytecodeIndices(bcIndex, bcIndices, middle + 1, high);
            }
            return bcIndex;
        }
    }
