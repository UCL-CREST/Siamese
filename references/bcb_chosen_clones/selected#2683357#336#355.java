    protected final short get_action(int state, int sym) {
        short tag;
        int first, last, probe;
        short[] row = action_tab[state];
        if (row.length < 20) for (probe = 0; probe < row.length; probe++) {
            tag = row[probe++];
            if (tag == sym || tag == -1) {
                return row[probe];
            }
        } else {
            first = 0;
            last = (row.length - 1) / 2 - 1;
            while (first <= last) {
                probe = (first + last) / 2;
                if (sym == row[probe * 2]) return row[probe * 2 + 1]; else if (sym > row[probe * 2]) first = probe + 1; else last = probe - 1;
            }
            return row[row.length - 1];
        }
        return 0;
    }
