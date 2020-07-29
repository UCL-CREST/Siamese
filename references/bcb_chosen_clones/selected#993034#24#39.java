    private Pixel search(int pixVal) {
        int low = 0;
        int high = length - 1;
        int mid;
        while (low <= high) {
            mid = (low + high) / 2;
            if (arrayCache[mid].intValue > pixVal) {
                high = mid - 1;
            } else if (arrayCache[mid].intValue < pixVal) {
                low = mid + 1;
            } else {
                return arrayCache[mid];
            }
        }
        return null;
    }
