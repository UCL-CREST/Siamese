    public void build(Dataset data) {
        m_eigenvalues = null;
        m_sumOfEigenValues = 0.0;
        Vector<Integer> deleteCols = new Vector<Integer>();
        for (int i = 0; i < data.instance(0).size(); i++) {
            if (numDistinctValues(i, data) <= 1) {
                deleteCols.addElement(i);
            }
        }
        Dataset m_trainInstances = data;
        if (deleteCols.size() > 0) {
            int[] todelete = new int[deleteCols.size()];
            for (int i = 0; i < deleteCols.size(); i++) {
                todelete[i] = deleteCols.get(i);
            }
            remAtt = new RemoveAttributes(todelete);
            m_trainInstances = remAtt.filterDataset(data);
        }
        m_numInstances = m_trainInstances.size();
        m_numAttribs = m_trainInstances.instance(0).size();
        fillCorrelation(m_trainInstances);
        double[] d = new double[m_numAttribs];
        double[][] V = new double[m_numAttribs][m_numAttribs];
        Matrix corr = new Matrix(m_correlation);
        EigenvalueDecomposition eig = new EigenvalueDecomposition(corr);
        Matrix v = eig.getV();
        double[] d2 = eig.getRealEigenvalues();
        int nr = v.getRowDimension();
        int nc = v.getColumnDimension();
        for (int i = 0; i < nr; i++) for (int j = 0; j < nc; j++) V[i][j] = v.get(i, j);
        for (int i = 0; i < d2.length; i++) d[i] = d2[i];
        m_eigenvectors = (double[][]) V.clone();
        m_eigenvalues = (double[]) d.clone();
        for (int i = 0; i < m_eigenvalues.length; i++) {
            if (m_eigenvalues[i] < 0) {
                m_eigenvalues[i] = 0.0;
            }
        }
        m_sortedEigens = Utils.sort(m_eigenvalues);
        m_sumOfEigenValues = Utils.sum(m_eigenvalues);
        double[][] orderedVectors = new double[m_eigenvectors.length][m_eigenvectors[0].length];
        for (int i = m_numAttribs; i > (m_numAttribs - m_eigenvectors[0].length); i--) {
            for (int j = 0; j < m_numAttribs; j++) {
                orderedVectors[j][m_numAttribs - i] = m_eigenvectors[j][m_sortedEigens[i - 1]];
            }
        }
        nr = orderedVectors.length;
        nc = orderedVectors[0].length;
        m_eTranspose = new double[nc][nr];
        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                m_eTranspose[i][j] = orderedVectors[j][i];
            }
        }
    }
