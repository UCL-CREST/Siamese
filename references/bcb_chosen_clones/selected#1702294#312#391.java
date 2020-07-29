    private void buildAttributeConstructor(Instances data) throws Exception {
        m_eigenvalues = null;
        m_outputNumAtts = -1;
        m_attributeFilter = null;
        m_nominalToBinFilter = null;
        m_sumOfEigenValues = 0.0;
        if (data.checkForStringAttributes()) {
            throw new UnsupportedAttributeTypeException("Can't handle string attributes!");
        }
        m_trainInstances = data;
        m_trainCopy = new Instances(m_trainInstances);
        m_replaceMissingFilter = new ReplaceMissingValues();
        m_replaceMissingFilter.setInputFormat(m_trainInstances);
        m_trainInstances = Filter.useFilter(m_trainInstances, m_replaceMissingFilter);
        if (m_normalize) {
            m_normalizeFilter = new Normalize();
            m_normalizeFilter.setInputFormat(m_trainInstances);
            m_trainInstances = Filter.useFilter(m_trainInstances, m_normalizeFilter);
        }
        m_nominalToBinFilter = new NominalToBinary();
        m_nominalToBinFilter.setInputFormat(m_trainInstances);
        m_trainInstances = Filter.useFilter(m_trainInstances, m_nominalToBinFilter);
        Vector deleteCols = new Vector();
        for (int i = 0; i < m_trainInstances.numAttributes(); i++) {
            if (m_trainInstances.numDistinctValues(i) <= 1) {
                deleteCols.addElement(new Integer(i));
            }
        }
        if (m_trainInstances.classIndex() >= 0) {
            m_hasClass = true;
            m_classIndex = m_trainInstances.classIndex();
            deleteCols.addElement(new Integer(m_classIndex));
        }
        if (deleteCols.size() > 0) {
            m_attributeFilter = new Remove();
            int[] todelete = new int[deleteCols.size()];
            for (int i = 0; i < deleteCols.size(); i++) {
                todelete[i] = ((Integer) (deleteCols.elementAt(i))).intValue();
            }
            m_attributeFilter.setAttributeIndicesArray(todelete);
            m_attributeFilter.setInvertSelection(false);
            m_attributeFilter.setInputFormat(m_trainInstances);
            m_trainInstances = Filter.useFilter(m_trainInstances, m_attributeFilter);
        }
        m_numInstances = m_trainInstances.numInstances();
        m_numAttribs = m_trainInstances.numAttributes();
        fillCorrelation();
        double[] d = new double[m_numAttribs];
        double[][] v = new double[m_numAttribs][m_numAttribs];
        Matrix corr = new Matrix(m_correlation);
        corr.eigenvalueDecomposition(v, d);
        m_eigenvectors = (double[][]) v.clone();
        m_eigenvalues = (double[]) d.clone();
        for (int i = 0; i < m_eigenvalues.length; i++) {
            if (m_eigenvalues[i] < 0) {
                m_eigenvalues[i] = 0.0;
            }
        }
        m_sortedEigens = Utils.sort(m_eigenvalues);
        m_sumOfEigenValues = Utils.sum(m_eigenvalues);
        m_transformedFormat = setOutputFormat();
        if (m_transBackToOriginal) {
            m_originalSpaceFormat = setOutputFormatOriginal();
            int numVectors = (m_transformedFormat.classIndex() < 0) ? m_transformedFormat.numAttributes() : m_transformedFormat.numAttributes() - 1;
            double[][] orderedVectors = new double[m_eigenvectors.length][numVectors + 1];
            for (int i = m_numAttribs - 1; i > (m_numAttribs - numVectors - 1); i--) {
                for (int j = 0; j < m_numAttribs; j++) {
                    orderedVectors[j][m_numAttribs - i] = m_eigenvectors[j][m_sortedEigens[i]];
                }
            }
            int nr = orderedVectors.length;
            int nc = orderedVectors[0].length;
            m_eTranspose = new double[nc][nr];
            for (int i = 0; i < nc; i++) {
                for (int j = 0; j < nr; j++) {
                    m_eTranspose[i][j] = orderedVectors[j][i];
                }
            }
        }
    }
