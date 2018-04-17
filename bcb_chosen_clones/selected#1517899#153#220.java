    private void preparePlot() {
        ClientDialog waitDialog = new ClientDialog(mainWin);
        waitDialog.setTitle("Calculating plot, please wait...");
        waitDialog.addJob(new Integer(1), alignmentResult.getNiceName(), "client-side", Task.JOBSTATUS_UNDERPROCESSING_STR, new Double(0));
        waitDialog.showMe();
        waitDialog.paintNow();
        if (mainWin.getParameterStorage().getGeneralParameters().getPeakMeasuringType() == GeneralParameters.PARAMETERVALUE_PEAKMEASURING_HEIGHT) {
            setTitle(alignmentResult.getNiceName() + ": CDA plot of average peak heights.");
        }
        if (mainWin.getParameterStorage().getGeneralParameters().getPeakMeasuringType() == GeneralParameters.PARAMETERVALUE_PEAKMEASURING_AREA) {
            setTitle(alignmentResult.getNiceName() + ": CDA plot of average peak areas.");
        }
        int numOfSamples = alignmentResult.getNumOfRawDatas();
        int numOfPeaks = alignmentResult.getNumOfRows();
        int numOfDim = alignmentResult.getNumOfFullRows();
        int[] rawDataIDs = alignmentResult.getRawDataIDs();
        String[] rawDataNames = new String[rawDataIDs.length];
        double[][] data = new double[numOfSamples][numOfDim];
        for (int sample = 0; sample < numOfSamples; sample++) {
            int rawDataID = rawDataIDs[sample];
            if (alignmentResult.isImported()) {
                rawDataNames[sample] = alignmentResult.getImportedRawDataName(rawDataID);
            } else {
                rawDataNames[sample] = mainWin.getItemSelector().getRawDataByID(rawDataID).getNiceName();
            }
            int colInd = 0;
            for (int peak = 0; peak < numOfPeaks; peak++) {
                if (!(alignmentResult.isFullRow(peak))) {
                    continue;
                }
                if (mainWin.getParameterStorage().getGeneralParameters().getPeakMeasuringType() == GeneralParameters.PARAMETERVALUE_PEAKMEASURING_HEIGHT) {
                    data[sample][colInd] = alignmentResult.getPeakHeight(rawDataID, peak);
                }
                if (mainWin.getParameterStorage().getGeneralParameters().getPeakMeasuringType() == GeneralParameters.PARAMETERVALUE_PEAKMEASURING_AREA) {
                    data[sample][colInd] = alignmentResult.getPeakArea(rawDataID, peak);
                }
                colInd++;
            }
        }
        double[][] dataT = new double[data[0].length][data.length];
        for (int sample = 0; sample < data.length; sample++) {
            for (int dim = 0; dim < data[0].length; dim++) {
                dataT[dim][sample] = (data[sample][dim]);
            }
        }
        waitDialog.updateJobStatus(new Integer(1), Task.JOBSTATUS_UNDERPROCESSING_STR, new Double(0.25));
        waitDialog.paintNow();
        double[][] dataT2 = Preprocessor.autoScaleToUnityVariance(dataT);
        waitDialog.updateJobStatus(new Integer(1), Task.JOBSTATUS_UNDERPROCESSING_STR, new Double(0.50));
        waitDialog.paintNow();
        for (int sample = 0; sample < data.length; sample++) {
            for (int dim = 0; dim < data[0].length; dim++) {
                data[sample][dim] = dataT2[dim][sample];
            }
        }
        dataT2 = null;
        waitDialog.updateJobStatus(new Integer(1), Task.JOBSTATUS_UNDERPROCESSING_STR, new Double(0.75));
        waitDialog.paintNow();
        CDA projector = new CDA(data, myParameters.paramAlpha, myParameters.paramLambda, myParameters.paramMaximumLoss, myParameters.paramTrainingLength, myParameters.paramNeighbourhoodSize, 2);
        for (int i = 0; i < myParameters.paramTrainingLength; i++) {
            projector.iterate();
        }
        double[][] results = projector.getState();
        waitDialog.updateJobStatus(new Integer(1), Task.JOBSTATUS_UNDERPROCESSING_STR, new Double(0.99));
        waitDialog.paintNow();
        waitDialog.hideMe();
        plotArea.setData(results[0], results[1], sampleClasses, rawDataNames);
    }
