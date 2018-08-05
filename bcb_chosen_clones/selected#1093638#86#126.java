    protected XYDataset createDataset(boolean isDemo) {
        if (isDemo) {
            updateStatus("isDemo==true in " + this.getClass().getName() + " class! return null Dataset, check the code!");
            return null;
        } else {
            setArrayFromTable();
            double[][] raw_xvalue;
            row_count = xyLength;
            raw_x2 = new String[independentVarLength][row_count];
            raw_xvalue = new double[independentVarLength][row_count];
            boolean[][] skip = new boolean[independentVarLength][row_count];
            for (int index = 0; index < independentVarLength; index++) {
                for (int i = 0; i < xyLength; i++) {
                    raw_x2[index][i] = indepValues[i][index];
                    try {
                        if (raw_x2[index][i] != "null" && raw_x2[index][i] != null && raw_x2[index][i].length() != 0) {
                            raw_xvalue[index][i] = Double.parseDouble(raw_x2[index][i]);
                        } else skip[index][i] = true;
                    } catch (Exception e) {
                        System.out.println("wrong data " + raw_x2[index][i]);
                    }
                }
            }
            rangeLabel = "";
            for (int j = 0; j < independentVarLength; j++) rangeLabel += independentHeaders[j] + "/";
            rangeLabel = rangeLabel.substring(0, rangeLabel.length() - 1);
            double[][] y_freq = new double[independentVarLength][row_count];
            for (int j = 0; j < independentVarLength; j++) {
                for (int i = 0; i < row_count; i++) y_freq[j][i] = i + 1;
            }
            XYSeriesCollection dataset = new XYSeriesCollection();
            for (int j = 0; j < independentVarLength; j++) {
                XYSeries series = new XYSeries(independentHeaders[j]);
                for (int i = 0; i < row_count; i++) {
                    if (skip[j][i] == false) series.add(y_freq[j][i], raw_xvalue[j][i]);
                }
                dataset.addSeries(series);
            }
            return dataset;
        }
    }
