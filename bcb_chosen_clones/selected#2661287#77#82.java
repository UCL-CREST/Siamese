    public synchronized void transpose() throws MatrixException {
        if (getDim().width != getDim().height) throw new MatrixException("transpose only for square matrixes");
        boolean[][] newArray = new boolean[getDim().width][getDim().height];
        for (int i = 0; i < theArray.length; i++) for (int j = 0; j < theArray[i].length; j++) newArray[j][i] = theArray[i][j];
        theArray = newArray;
    }
