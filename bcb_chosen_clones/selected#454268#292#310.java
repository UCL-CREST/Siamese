    public static int findUpperBoundaryVariation(Variation var, ArrayList<Variation> variations) {
        if (variations == null) {
            return Integer.MAX_VALUE;
        }
        int top = 0;
        int bot = variations.size();
        int mid = 0;
        while (top < bot) {
            mid = (top + bot) / 2;
            if (var.compareTo(variations.get(mid)) < 0) {
                bot = mid - 1;
            } else if (var.compareTo(variations.get(mid)) > 0) {
                top = mid + 1;
            } else {
                return mid;
            }
        }
        return Utils.max2(0, Utils.min2(top, variations.size() - 1));
    }
