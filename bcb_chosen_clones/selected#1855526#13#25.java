    public static char precompose(char base, char comb) {
        int min = 0;
        int max = precompositions.length - 1;
        int mid;
        long sought = base << UNICODE_SHIFT | comb;
        long that;
        while (max >= min) {
            mid = (min + max) / 2;
            that = precompositions[mid][1] << UNICODE_SHIFT | precompositions[mid][2];
            if (that < sought) min = mid + 1; else if (that > sought) max = mid - 1; else return precompositions[mid][0];
        }
        return base;
    }
