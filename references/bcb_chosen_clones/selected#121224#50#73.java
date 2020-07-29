    private static int findClosestIndex(String[] data, String key, boolean ignoreCase) {
        int low = 0;
        int high = data.length - 1;
        int middle = -1;
        while (high > low) {
            middle = (low + high) / 2;
            int result;
            if (ignoreCase) {
                result = key.compareToIgnoreCase(data[middle]);
            } else {
                result = key.compareTo(data[middle]);
            }
            if (result == 0) {
                return middle;
            } else if (result < 0) {
                high = middle;
            } else if (low != middle) {
                low = middle;
            } else {
                break;
            }
        }
        return middle;
    }
