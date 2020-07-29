    public int lookupToken(int base, String key) {
        int start = fa.getSpecialCasesIndexes()[base][0];
        int end = fa.getSpecialCasesIndexes()[base][1] - 1;
        if (!sensitive) key = key.toUpperCase();
        while (start <= end) {
            int half = (start + end) / 2;
            int comp = fa.getSpecialCases()[half].key.compareTo(key);
            if (comp == 0) return fa.getSpecialCases()[half].value; else if (comp < 0) start = half + 1; else end = half - 1;
        }
        return base;
    }
