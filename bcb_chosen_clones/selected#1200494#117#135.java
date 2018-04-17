    private int indexOf(char c) {
        int start = 0;
        int end = intervalls.size() - 1;
        while (start <= end) {
            int check = (start + end) / 2;
            Interval i = (Interval) intervalls.get(check);
            if (start == end) return i.contains(c) ? start : -1;
            if (c < i.start) {
                end = check - 1;
                continue;
            }
            if (c > i.end) {
                start = check + 1;
                continue;
            }
            return check;
        }
        return -1;
    }
