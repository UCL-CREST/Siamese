    public String[][] getTextLines() {
        TableCell[] cells = getCellsArray();
        String[][] res = null;
        res = new String[cells.length][];
        int maxLinesCount = -1;
        int minLinesCount = -1;
        int emptyLinesCo = 0;
        for (int cIdx = 0; cIdx < cells.length; cIdx++) {
            TableCell tc = cells[cIdx];
            if (tc == null) {
                emptyLinesCo++;
                res[cIdx] = null;
                continue;
            }
            String[] lines = tc.getTextLines();
            res[cIdx] = lines;
            if (maxLinesCount < lines.length) {
                maxLinesCount = lines.length;
            }
            if (minLinesCount < 0 || minLinesCount > lines.length) {
                minLinesCount = lines.length;
            }
        }
        if (emptyLinesCo > 0) {
            if ((res.length - emptyLinesCo) <= 0) return new String[][] {};
            String[][] nres = new String[cells.length - emptyLinesCo][];
            TableCell[] ncells = new TableCell[cells.length - emptyLinesCo];
            int idx = -1;
            int cidx = -1;
            for (String[] lines : res) {
                cidx++;
                if (lines == null) continue;
                idx++;
                nres[idx] = lines;
                ncells[idx] = cells[cidx];
            }
            res = nres;
            cells = ncells;
        }
        for (int iBlock = 0; iBlock < res.length; iBlock++) {
            TableCell tc = cells[iBlock];
            String[] lines = res[iBlock];
            String[] nlines = lines;
            int addLinesCo = maxLinesCount - lines.length;
            if (addLinesCo > 0) {
                String[] fillText = getFillVertAlignText(tc, addLinesCo);
                Align a = getVertAlign(tc);
                if (a == Align.Begin) {
                    nlines = Arrays.copyOf(lines, lines.length + addLinesCo);
                    for (int i = 0; i < addLinesCo; i++) {
                        nlines[i + lines.length] = fillText[i];
                    }
                } else if (a == Align.End) {
                    nlines = new String[lines.length + addLinesCo];
                    for (int i = 0; i < addLinesCo; i++) nlines[i] = fillText[i];
                    System.arraycopy(lines, 0, nlines, addLinesCo, lines.length);
                } else if (a == Align.Center) {
                    List<String> llines = new ArrayList<String>(Arrays.asList(lines));
                    boolean insBegin = true;
                    for (int i = 0; i < addLinesCo; i++) {
                        if (insBegin) {
                            llines.add(0, fillText[i]);
                        } else {
                            llines.add(fillText[i]);
                        }
                        insBegin = !insBegin;
                    }
                    nlines = llines.toArray(new String[] {});
                }
            }
            res[iBlock] = nlines;
        }
        String[][] nres = new String[maxLinesCount][res.length];
        for (int iBlock = 0; iBlock < res.length; iBlock++) {
            for (int iLine = 0; iLine < maxLinesCount; iLine++) {
                nres[iLine][iBlock] = res[iBlock][iLine];
            }
        }
        res = nres;
        return res;
    }
