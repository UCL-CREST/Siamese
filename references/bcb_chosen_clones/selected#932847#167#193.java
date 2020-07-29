    public static int binarySearch(int[] data, int key, int low, int high) throws IllegalArgumentException {
        if (data == null) {
            throw new IllegalArgumentException("null array");
        }
        if (data.length == 0) {
            return -1;
        }
        if (low <= high && (low < 0 || high < 0)) {
            throw new IllegalArgumentException("can't search negative indices " + low + " " + high);
        }
        if (high > data.length - 1) {
            high = data.length - 1;
        }
        if (low <= high) {
            int mid = (low + high) / 2;
            int midValue = data[mid];
            if (midValue == key) {
                return mid;
            } else if (midValue > key) {
                return binarySearch(data, key, low, mid - 1);
            } else {
                return binarySearch(data, key, mid + 1, high);
            }
        } else {
            return -1;
        }
    }
