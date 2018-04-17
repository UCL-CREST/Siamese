    protected String shortenText(GC gc, String t, int width) {
        if (t == null) {
            return null;
        }
        int w = gc.textExtent(ELLIPSIS, DRAW_FLAGS).x;
        if (width <= w) {
            return t;
        }
        int l = t.length();
        int max = l / 2;
        int min = 0;
        int mid = (max + min) / 2 - 1;
        if (mid <= 0) {
            return t;
        }
        while (min < mid && mid < max) {
            String s1 = t.substring(0, mid);
            String s2 = t.substring(l - mid, l);
            int l1 = gc.textExtent(s1, DRAW_FLAGS).x;
            int l2 = gc.textExtent(s2, DRAW_FLAGS).x;
            if (l1 + w + l2 > width) {
                max = mid;
                mid = (max + min) / 2;
            } else if (l1 + w + l2 < width) {
                min = mid;
                mid = (max + min) / 2;
            } else {
                min = max;
            }
        }
        if (mid == 0) {
            return t;
        }
        return t.substring(0, mid) + ELLIPSIS + t.substring(l - mid, l);
    }
