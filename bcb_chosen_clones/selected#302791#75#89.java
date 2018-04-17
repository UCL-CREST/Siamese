    public boolean get(int bit) {
        int low = 0;
        int high = elements.length - 1;
        while (low <= high) {
            int middle = (low + high) / 2;
            if (bit < elements[middle]) {
                high = middle - 1;
            } else if (bit == elements[middle]) {
                return true;
            } else {
                low = middle + 1;
            }
        }
        return false;
    }
