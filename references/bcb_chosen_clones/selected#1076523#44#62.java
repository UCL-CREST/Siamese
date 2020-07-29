    public int binarySearch(long searchKey) {
        int lowerBound = 0;
        int upperBound = nElems - 1;
        int cruIn;
        while (true) {
            cruIn = (lowerBound + upperBound) / 2;
            if (a[cruIn] == searchKey) {
                return cruIn;
            } else if (lowerBound > upperBound) {
                return -1;
            } else {
                if (a[cruIn] < searchKey) {
                    lowerBound = cruIn + 1;
                } else {
                    upperBound = cruIn - 1;
                }
            }
        }
    }
