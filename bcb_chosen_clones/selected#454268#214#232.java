    public static int findUpperBoundary(int start, ArrayList<Tuple<Integer, HashSet<String>>> variations) {
        if (variations == null) {
            return Integer.MAX_VALUE;
        }
        int top = 0;
        int bot = variations.size();
        int mid = 0;
        while (top < bot) {
            mid = (top + bot) / 2;
            if (start < variations.get(mid).get_first()) {
                bot = mid - 1;
            } else if (start > variations.get(mid).get_first()) {
                top = mid + 1;
            } else {
                return mid;
            }
        }
        return Utils.min2(top, variations.size() - 1);
    }
