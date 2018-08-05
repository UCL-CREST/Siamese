    public int find(int p) {
        int start = 0;
        int end;
        int mid;
        int midpos;
        if (!scanning) {
            moveGap((gap + tokens.length) - endgap);
        }
        end = gap - 1;
        if (p > tokens[end].position) {
            return end;
        }
        while (end > (start + 1)) {
            mid = (start + end) / 2;
            midpos = tokens[mid].position;
            if (p > midpos) {
                start = mid;
            } else {
                end = mid;
            }
        }
        return start;
    }
