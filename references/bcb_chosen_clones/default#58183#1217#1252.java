    public static int binarySearch(Vector<possibleLayout> layouts, float cost, int low, int high) {
        int middle;
        float costTest;
        while (low <= high) {
            middle = (low + high) / 2;
            costTest = layouts.get(middle).newLayout.stateCost;
            if (costTest == cost) {
                return middle + 1;
            } else if (costTest > cost) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        if (low >= (layouts.size() - 1)) {
            costTest = layouts.get(layouts.size() - 1).newLayout.stateCost;
            if (costTest > cost) {
                return (layouts.size());
            } else {
                return layouts.size() - 1;
            }
        } else if (high <= 0) {
            costTest = layouts.get(0).newLayout.stateCost;
            if (costTest > cost) {
                return 0;
            } else {
                return 1;
            }
        } else {
            if (layouts.get(low).newLayout.stateCost < cost) {
                return low + 1;
            } else {
                return low;
            }
        }
    }
