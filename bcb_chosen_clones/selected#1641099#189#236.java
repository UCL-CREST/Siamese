    public void doAnalysis() {
        if (dataTable.isEditing()) dataTable.getCellEditor().stopCellEditing();
        if (!hasExample) {
            JOptionPane.showMessageDialog(this, DATA_MISSING_MESSAGE);
            return;
        }
        if (dependentIndex < 0) {
            JOptionPane.showMessageDialog(this, VARIABLE_MISSING_MESSAGE);
            return;
        }
        trimColumn = false;
        setArrayFromTable();
        values_storage = new String[dependentVarLength][xyLength];
        Data data = new Data();
        for (int index = 0; index < dependentVarLength; index++) {
            String[] xData = new String[xyLength];
            for (int i = 0; i < sampleSizes[index]; i++) {
                values_storage[index][i] = depValues[i][index];
                if (depValues[i][index] != null && depValues[i][index].length() != 0) xData[i] = depValues[i][index];
            }
            data.appendX(dependentHeaders[index], xData, DataType.QUANTITATIVE);
        }
        nTrials = dependentVarLength;
        alpha = confidenceControlPanel.getAlphaValue();
        cvIndex = confidenceControlPanel.getAlphaIndex();
        ci_choice = confidenceControlPanel.whichIntervalSelected();
        left = confidenceControlPanel.getLeftCutOffValue();
        right = confidenceControlPanel.getRightCutOffValue();
        knownVariance = confidenceControlPanel.getKnownVariance();
        data.setParameter(AnalysisType.CI, ConfidenceInterval.CI_N_TRAILS, nTrials + "");
        data.setParameter(AnalysisType.CI, ConfidenceInterval.CI_XY_LENGTH, xyLength + "");
        data.setParameter(AnalysisType.CI, ConfidenceInterval.CI_CV_INDEX, cvIndex + "");
        data.setParameter(AnalysisType.CI, ConfidenceInterval.CI_CHOICE, ci_choice);
        data.setParameter(AnalysisType.CI, ConfidenceInterval.CI_LEFT, left + "");
        data.setParameter(AnalysisType.CI, ConfidenceInterval.CI_RIGHT, right + "");
        data.setParameter(AnalysisType.CI, ConfidenceInterval.CI_KNOWN_VARIANCE, knownVariance + "");
        result = null;
        try {
            result = (ConfidenceIntervalResult) data.getAnalysis(AnalysisType.CI);
        } catch (Exception e) {
        }
        xBar = new double[nTrials];
        ciData = result.getCIData();
        sampleData = result.getSampleData();
        xBar = result.getXBar();
        doGraph();
        updateResults();
    }
