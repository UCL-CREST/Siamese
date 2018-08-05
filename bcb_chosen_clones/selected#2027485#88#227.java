    private double[][] covarianceOptimize() throws InterruptedException {
        double dSum = 0;
        int iValues = 0;
        buildGrid();
        double[][] coV = new double[inputGrid.getRasterBuf().getBandCount()][inputGrid.getRasterBuf().getBandCount()];
        double cancelMatrix[][] = new double[][] { { 0 } };
        double valorBandai = 0, valorBandaj = 0;
        int bandCount = inputGrid.getRasterBuf().getBandCount();
        if (inputGrid.getRasterBuf().getDataType() == RasterBuffer.TYPE_BYTE) {
            for (int i = 0; i < bandCount; i++) {
                for (int j = i; j < bandCount; j++) {
                    if (cancel) return cancelMatrix;
                    iValues = 0;
                    dSum = 0;
                    for (int k = 0; k < inputGrid.getNX(); k++) {
                        for (int l = 0; l < inputGrid.getNY(); l++) {
                            try {
                                inputGrid.setBandToOperate(i);
                                valorBandai = inputGrid.getCellValueAsByte(k, l) - inputGrid.getMeanValue();
                                inputGrid.setBandToOperate(j);
                                valorBandaj = inputGrid.getCellValueAsByte(k, l) - inputGrid.getMeanValue();
                            } catch (GridException e) {
                                RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
                            }
                            dSum += valorBandai * valorBandaj;
                            iValues++;
                        }
                    }
                    if (iValues > 1) coV[i][j] = dSum / (double) (iValues); else coV[i][j] = inputGrid.getNoDataValue();
                }
                if (bandCount > 1) percent = (i + 1) * 100 / (bandCount - 1); else percent = (i + 1) * 100 / (1);
            }
        }
        if (inputGrid.getRasterBuf().getDataType() == RasterBuffer.TYPE_SHORT) {
            for (int i = 0; i < bandCount; i++) {
                for (int j = i; j < bandCount; j++) {
                    if (cancel) return cancelMatrix;
                    iValues = 0;
                    dSum = 0;
                    for (int k = 0; k < inputGrid.getNX(); k++) {
                        for (int l = 0; l < inputGrid.getNY(); l++) {
                            try {
                                inputGrid.setBandToOperate(i);
                                valorBandai = inputGrid.getCellValueAsShort(k, l) - inputGrid.getMeanValue();
                                inputGrid.setBandToOperate(j);
                                valorBandaj = inputGrid.getCellValueAsShort(k, l) - inputGrid.getMeanValue();
                            } catch (GridException e) {
                                RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
                            }
                            dSum += valorBandai * valorBandaj;
                            iValues++;
                        }
                    }
                    if (iValues > 1) coV[i][j] = dSum / (double) (iValues); else coV[i][j] = inputGrid.getNoDataValue();
                }
                if (bandCount > 1) percent = (i + 1) * 100 / (bandCount - 1); else percent = (i + 1) * 100 / (1);
            }
        }
        if (inputGrid.getRasterBuf().getDataType() == RasterBuffer.TYPE_INT) {
            for (int i = 0; i < bandCount; i++) {
                for (int j = i; j < bandCount; j++) {
                    if (cancel) return cancelMatrix;
                    iValues = 0;
                    dSum = 0;
                    for (int k = 0; k < inputGrid.getNX(); k++) {
                        for (int l = 0; l < inputGrid.getNY(); l++) {
                            try {
                                inputGrid.setBandToOperate(i);
                                valorBandai = inputGrid.getCellValueAsInt(k, l) - inputGrid.getMeanValue();
                                inputGrid.setBandToOperate(j);
                                valorBandaj = inputGrid.getCellValueAsInt(k, l) - inputGrid.getMeanValue();
                            } catch (GridException e) {
                                RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
                            }
                            dSum += valorBandai * valorBandaj;
                            iValues++;
                        }
                    }
                    if (iValues > 1) coV[i][j] = dSum / (double) (iValues); else coV[i][j] = inputGrid.getNoDataValue();
                }
                if (bandCount > 1) percent = (i + 1) * 100 / (bandCount - 1); else percent = (i + 1) * 100 / (1);
            }
        }
        if (inputGrid.getRasterBuf().getDataType() == RasterBuffer.TYPE_FLOAT) {
            for (int i = 0; i < bandCount; i++) {
                for (int j = i; j < bandCount; j++) {
                    if (cancel) return cancelMatrix;
                    iValues = 0;
                    dSum = 0;
                    for (int k = 0; k < inputGrid.getNX(); k++) {
                        for (int l = 0; l < inputGrid.getNY(); l++) {
                            try {
                                inputGrid.setBandToOperate(i);
                                valorBandai = inputGrid.getCellValueAsFloat(k, l) - inputGrid.getMeanValue();
                                inputGrid.setBandToOperate(j);
                                valorBandaj = inputGrid.getCellValueAsFloat(k, l) - inputGrid.getMeanValue();
                            } catch (GridException e) {
                                RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
                            }
                            dSum += valorBandai * valorBandaj;
                            iValues++;
                        }
                    }
                    if (iValues > 1) coV[i][j] = dSum / (double) (iValues); else coV[i][j] = inputGrid.getNoDataValue();
                }
                if (bandCount > 1) percent = (i + 1) * 100 / (bandCount - 1); else percent = (i + 1) * 100 / (1);
            }
        }
        if (inputGrid.getRasterBuf().getDataType() == RasterBuffer.TYPE_DOUBLE) {
            for (int i = 0; i < bandCount; i++) {
                for (int j = i; j < bandCount; j++) {
                    if (cancel) return cancelMatrix;
                    iValues = 0;
                    dSum = 0;
                    for (int k = 0; k < inputGrid.getNX(); k++) {
                        for (int l = 0; l < inputGrid.getNY(); l++) {
                            try {
                                inputGrid.setBandToOperate(i);
                                valorBandai = inputGrid.getCellValueAsDouble(k, l) - inputGrid.getMeanValue();
                                inputGrid.setBandToOperate(j);
                                valorBandaj = inputGrid.getCellValueAsDouble(k, l) - inputGrid.getMeanValue();
                            } catch (GridException e) {
                                RasterToolsUtil.messageBoxError(PluginServices.getText(this, "grid_error"), this, e);
                            }
                            dSum += valorBandai * valorBandaj;
                            iValues++;
                        }
                    }
                    if (iValues > 1) coV[i][j] = dSum / (double) (iValues); else coV[i][j] = inputGrid.getNoDataValue();
                }
                if (bandCount > 1) percent = (i + 1) * 100 / (bandCount - 1); else percent = (i + 1) * 100 / (1);
            }
        }
        for (int i = 0; i < bandCount; i++) {
            for (int j = 0; j < bandCount; j++) {
                if (j < i) coV[i][j] = coV[j][i];
            }
        }
        return coV;
    }
