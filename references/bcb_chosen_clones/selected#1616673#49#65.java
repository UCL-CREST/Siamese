    private Object binarySearch(String s, int i, int j) {
        try {
            if (i > j) {
                return null;
            }
            int k = (i + j) / 2;
            if (s.equals(elementAt(k).toString())) {
                return elementAt(k);
            }
            if (s.compareTo(elementAt(k).toString()) < 0) {
                return binarySearch(s, i, k - 1);
            }
            return binarySearch(s, k + 1, j);
        } catch (IndexOutOfBoundsException indexoutofboundsexception) {
            return null;
        }
    }
