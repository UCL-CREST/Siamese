    public int getLineAtOffset(int offset) {
        if (offset == 0 || regionCount <= 1) {
            return 0;
        }
        if (offset == document.getLength()) {
            return regionCount - 1;
        }
        int left = 0;
        int right = regionCount - 1;
        int midIndex = 0;
        while (left <= right) {
            midIndex = (left + right) / 2;
            if (offset < offsets[midIndex]) {
                right = midIndex;
            } else if (offset >= offsets[midIndex] + lengths[midIndex]) {
                left = midIndex + 1;
            } else {
                return midIndex;
            }
        }
        return midIndex;
    }
