    protected int findUpperChildPosition(int position) {
        int start = 0, end = fChildren.size() - 1;
        while (start < end) {
            int middlePosition = start + (end - start) / 2;
            T middle = fChildren.get(middlePosition);
            if (position < middle.fOffset) {
                end = middlePosition;
            } else if (position >= middle.fOffset + middle.fLength) {
                start = middlePosition + 1;
            } else return middlePosition;
        }
        if (end < start || position > fChildren.get(start).getEnd()) return -1; else return start;
    }
