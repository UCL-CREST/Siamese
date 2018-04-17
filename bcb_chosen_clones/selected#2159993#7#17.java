    public static int translate(int unicode) {
        if ((unicode >= 0x20 && unicode <= 0x7e) || (unicode >= 0xa0 && unicode <= 0xff)) return unicode;
        int min = 0;
        int max = table.length - 1;
        int mid;
        while (max >= min) {
            mid = (min + max) / 2;
            if (table[mid][1] < unicode) min = mid + 1; else if (table[mid][1] > unicode) max = mid - 1; else return table[mid][0];
        }
        return -1;
    }
