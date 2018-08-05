    private int getIndexListPosition(int index) {
        int bottom = 0;
        int top = getSize() - 1;
        int center = (top + bottom) / 2;
        while (indexList[center] != index && bottom < top) {
            if (indexList[center] > index) {
                top = center - 1;
            } else {
                bottom = center + 1;
            }
            center = (top + bottom) / 2;
        }
        return center;
    }
