    private long search(LongArrayList matches, long value, boolean onReverse) {
        int left = 0;
        int right = matches.size() - 1;
        int middle = right / 2;
        long distance;
        while (left <= right) {
            if (onReverse) {
                distance = matches.getQuick(middle) + readLength - value;
            } else {
                distance = value + readLength - matches.getQuick(middle);
            }
            if (distance >= minDistance && distance <= maxDistance) {
                return matches.getQuick(middle);
            }
            if (distance < minDistance) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
            middle = (left + right) / 2;
        }
        return Constants.INVALID;
    }
