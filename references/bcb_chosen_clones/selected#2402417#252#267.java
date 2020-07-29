    private int search(long key, int low, int high) {
        if (low > high) {
            throw new IllegalArgumentException("low should be <= high; low == " + low + ", high == " + high + ", key == " + key + ", handleToLast == " + handleToLast);
        }
        for (; ; ) {
            int result = (low + high) / 2;
            if (low >= high || keys[result] == key) {
                return result;
            }
            if (key < keys[result]) {
                high = result - 1;
            } else {
                low = result + 1;
            }
        }
    }
