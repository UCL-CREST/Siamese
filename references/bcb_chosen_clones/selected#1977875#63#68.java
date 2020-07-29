    private double[][] classesArray(Dataset learn) {
        double[][] ret = new double[learn.getN()][learn.getClassCount()];
        double[][] columns = learn.getColumns();
        for (int i = 0; i < learn.getN(); i++) for (int c = 0; c < learn.getClassCount(); c++) ret[i][c] = columns[c][i];
        return ret;
    }
