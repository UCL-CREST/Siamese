    public double[][] getMatrix() {
        double[][] d = new double[super.numInstances()][super.numAttributes()];
        String[] s = new String[super.numAttributes()];
        for (int i = 0; i < s.length; i++) {
            s[i] = (super.attribute(i)).name();
        }
        double[][] t = getDescValues(s);
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[i].length; j++) {
                d[j][i] = t[i][j];
            }
        }
        return d;
    }
