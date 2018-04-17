    private void computeMImatrix() {
        int numInAtt = NUM_ATTRIBUTES - 1;
        m_MImatrix = new double[numInAtt][numInAtt];
        for (int i = 0; i < numInAtt; i++) {
            for (int j = i; j < numInAtt; j++) {
                m_MImatrix[i][j] = getAttAttMI(i, j);
                m_MImatrix[j][i] = m_MImatrix[i][j];
            }
        }
    }
