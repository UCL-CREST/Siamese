    public static int findStringHit(String txt, int x, FontMetrics fm, boolean alwaysmin) {
        if (x <= 0) return 0;
        int min = 0;
        int max = txt.length();
        int lmin = 0;
        int lmax = fm.stringWidth(txt);
        while (max - min > 1) {
            int cur = (max + min) / 2;
            int curlen = fm.stringWidth(txt.substring(0, cur));
            p("Iter: " + cur + " " + curlen + " " + max + " " + lmax + " " + min + " " + lmin);
            if (curlen > x) {
                max = cur;
                lmax = curlen;
            } else {
                min = cur;
                lmin = curlen;
            }
        }
        int mx = lmax - x;
        int mn = x - lmin;
        if (mn < mx || alwaysmin) return min; else return max;
    }
