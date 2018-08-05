    public GCGCDatum[][] toArray() {
        GCGCDatum tempArray[][] = new GCGCDatum[getNumberRows()][];
        GCGCDatum returnedArray[][] = new GCGCDatum[getNumberCols()][getNumberRows()];
        for (int i = 0; i < getNumberRows(); i++) {
            tempArray[i] = ((List<GCGCDatum>) peakList.get(i).getVar("getDatumArray")).toArray(new GCGCDatum[0]);
        }
        for (int i = 0; i < getNumberRows(); i++) {
            for (int j = 0; j < getNumberCols(); j++) {
                returnedArray[j][i] = tempArray[i][j];
            }
        }
        return returnedArray;
    }
