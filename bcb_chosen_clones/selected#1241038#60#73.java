    private int findInCumulativeProb(double num) {
        int min = 0;
        int max = cumulativeProb.length - 1;
        int ptr;
        while (true) {
            ptr = (max + min) / 2;
            if (num >= cumulativeProb[ptr]) {
                if (max - min == 1) return max;
                min = ptr;
            } else if (ptr != 0 && num < cumulativeProb[ptr - 1]) {
                max = ptr;
            } else return ptr;
        }
    }
