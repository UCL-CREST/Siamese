    private int getChromosomPositionInArray(ArrayList s, String[] values) {
        int low = 0;
        int high = s.size() - 1;
        int pos = low;
        int tmpChr;
        String c = values[2];
        if (c.equalsIgnoreCase("X")) tmpChr = Sample.X; else if (c.equalsIgnoreCase("Y")) tmpChr = Sample.Y; else tmpChr = (new Integer(c)).intValue();
        tmpChr--;
        while (low <= high) {
            int mid = (low + high) / 2;
            int chromosome = ((Sample) s.get(mid)).getChromosomeID() - 1;
            if (chromosome > tmpChr) {
                high = mid - 1;
            } else if (chromosome < tmpChr) {
                low = mid + 1;
                pos = low;
            } else {
                pos = mid;
                break;
            }
        }
        if (s.size() != 0 && pos > 0) {
            while (pos < s.size() && pos > 0 && ((Sample) s.get(pos - 1)).getChromosomeID() - 1 == tmpChr) pos--;
            while (pos < s.size() && ((Sample) s.get(pos)).getChromosomeID() - 1 == tmpChr) {
                int v = Integer.parseInt(values[3]);
                int map = ((Sample) s.get(pos)).getGeneStart();
                if (v > map) pos++; else return pos;
            }
        }
        return pos;
    }
