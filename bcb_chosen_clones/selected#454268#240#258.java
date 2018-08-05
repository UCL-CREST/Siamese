    public static int findUpperBoundarySNP(int start, ArrayList<Tuple<SNP, HashSet<String>>> variations) {
        if (variations == null) {
            return Integer.MAX_VALUE;
        }
        int top = 0;
        int bot = variations.size();
        int mid = 0;
        while (top < bot) {
            mid = (top + bot) / 2;
            if (start < variations.get(mid).get_first().get_position()) {
                bot = mid - 1;
            } else if (start > variations.get(mid).get_first().get_position()) {
                top = mid + 1;
            } else {
                return mid;
            }
        }
        return Utils.max2(0, Utils.min2(top, variations.size() - 1));
    }
