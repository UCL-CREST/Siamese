    public int find_globalid(String name) {
        int lo, hi, mid, val;
        CardInfo info;
        lo = 0;
        hi = cards.length - 1;
        while (lo <= hi) {
            mid = (lo + hi) / 2;
            val = cards[mid].name.compareToIgnoreCase(name);
            if (val == 0) return (mid);
            if (val < 0) lo = mid + 1; else hi = mid - 1;
        }
        for (mid = 0; mid < extras.size(); mid++) {
            info = (CardInfo) extras.get(mid);
            if (info.name.equalsIgnoreCase(name)) return (cards.length + mid);
        }
        return (add_extra(name));
    }
