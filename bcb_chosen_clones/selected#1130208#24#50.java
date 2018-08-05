    public int getLocation(SeqFeatureI feature) {
        int bot = 0;
        int top = features.size() - 1;
        int low = feature.getLow();
        int high = feature.getHigh();
        while (bot <= top) {
            int mid = (bot + top) / 2;
            SeqFeatureI midFeature = (SeqFeatureI) features.elementAt(mid);
            int midLow = midFeature.getLow();
            int cmp = 0;
            if (midLow > low) {
                cmp = 1;
            } else if (midLow < low) {
                cmp = -1;
            }
            if (cmp == 0) {
                int midHigh = midFeature.getHigh();
                if (midHigh > high) {
                    cmp = 1;
                } else if (midHigh < high) {
                    cmp = -1;
                }
            }
            if (cmp < 0) bot = mid + 1; else if (cmp > 0) top = mid - 1; else return mid;
        }
        return bot;
    }
