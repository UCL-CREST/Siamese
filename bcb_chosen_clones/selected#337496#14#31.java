    static char charshape(char s, int which) {
        int l, r, m;
        if ((s >= 0x0621) && (s <= 0x06D3)) {
            l = 0;
            r = chartable.length - 1;
            while (l <= r) {
                m = (l + r) / 2;
                if (s == chartable[m][0]) {
                    return chartable[m][which + 1];
                } else if (s < chartable[m][0]) {
                    r = m - 1;
                } else {
                    l = m + 1;
                }
            }
        } else if (s >= 0xfef5 && s <= 0xfefb) return (char) (s + which);
        return s;
    }
