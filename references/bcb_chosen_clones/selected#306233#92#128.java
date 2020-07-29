    public void calculateAdvancedStatistic() throws GridException, InterruptedException {
        if (!isStatisticsCalculated()) calculateStatistics();
        int bandCount = roi.getBandCount();
        double dSum[][] = new double[bandCount][bandCount];
        double iValues[][] = new double[bandCount][bandCount];
        varCov = new double[bandCount][bandCount];
        double valorBandai = 0, valorBandaj = 0;
        for (int iBand = 0; iBand < bandCount; iBand++) for (int jBand = 0; jBand < bandCount; jBand++) {
            dSum[iBand][jBand] = 0;
            iValues[iBand][jBand] = 0;
        }
        for (int k = 0; k < roi.getNY(); k++) {
            for (int l = 0; l < roi.getNX(); l++) {
                for (int i = 0; i < bandCount; i++) {
                    for (int j = i; j < bandCount; j++) {
                        roi.setBandToOperate(i);
                        valorBandai = getValue(l, k);
                        roi.setBandToOperate(j);
                        valorBandaj = getValue(l, k);
                        if (!roi.isNoDataValue(valorBandai) && !roi.isNoDataValue(valorBandaj)) {
                            valorBandai = valorBandai - mean[i];
                            valorBandaj = valorBandaj - mean[j];
                            dSum[i][j] += valorBandai * valorBandaj;
                            iValues[i][j]++;
                        }
                    }
                }
            }
        }
        for (int iBand = 0; iBand < bandCount; iBand++) for (int jBand = 0; jBand < bandCount; jBand++) if (iValues[iBand][jBand] > 1) varCov[iBand][jBand] = dSum[iBand][jBand] / (double) (iValues[iBand][jBand]); else varCov[iBand][jBand] = roi.getGrid().getNoDataValue();
        for (int i = 0; i < bandCount; i++) {
            for (int j = 0; j < bandCount; j++) {
                if (j < i) varCov[i][j] = varCov[j][i];
            }
        }
        advancedStatisticsCalculated = true;
    }
