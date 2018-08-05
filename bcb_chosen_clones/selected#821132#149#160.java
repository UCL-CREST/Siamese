    public int find_card(CardInfo[] cardbase, String name) {
        int lo, hi, mid, val;
        lo = 0;
        hi = cardbase.length - 1;
        while (lo <= hi) {
            mid = (lo + hi) / 2;
            val = cardbase[mid].name.compareToIgnoreCase(name);
            if (val == 0) return (mid);
            if (val < 0) lo = mid + 1; else hi = mid - 1;
        }
        return (-1);
    }
