    public int findProbabilityID(int thirdWordID) {
        int mid, start = 0, end = getNumberNGrams();
        int trigram = -1;
        while ((end - start) > 0) {
            mid = (start + end) / 2;
            int midWordID = getWordID(mid);
            if (midWordID < thirdWordID) {
                start = mid + 1;
            } else if (midWordID > thirdWordID) {
                end = mid;
            } else {
                trigram = getProbabilityID(mid);
                break;
            }
        }
        return trigram;
    }
