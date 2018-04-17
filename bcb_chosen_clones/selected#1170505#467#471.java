    public Matrix transpose() throws JasymcaException {
        Algebraic b[][] = new Algebraic[a[0].length][a.length];
        for (int i = 0; i < a.length; i++) for (int k = 0; k < a[0].length; k++) b[k][i] = a[i][k];
        return new Matrix(b);
    }
