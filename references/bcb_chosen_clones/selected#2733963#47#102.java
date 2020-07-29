    public static void main(String args[]) {
        try {
            DataFrame df = ImportData.importXLS("G:/Recerca/EIO_RECERCA/New CoDaPack/Halimba.xls", true);
            String[] names = new String[3];
            names[0] = df.getName(0);
            names[1] = df.getName(1);
            names[2] = df.getName(2);
            double[][] data = new double[3][];
            data[0] = df.getNumericalData(names[0]);
            data[1] = df.getNumericalData(names[1]);
            data[2] = df.getNumericalData(names[2]);
            int size = data[0].length;
            TernaryPlot2dWindow frame = new TernaryPlot2dWindow(df, new TernaryPlot2dBuilder(names, data).build(), "TEST");
            frame.setSize(600, 600);
            frame.setVisible(true);
            double cdata[][] = new double[3][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < 3; j++) {
                    cdata[j][i] = Math.log(data[j][i]);
                }
            }
            double mean[] = { 0, 0, 0 };
            double cov[][] = new double[3][3];
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < size; k++) {
                    mean[j] += cdata[j][k];
                }
                mean[j] /= size;
            }
            for (int j = 0; j < 3; j++) {
                for (int i = j; i < 3; i++) {
                    cov[i][j] = 0;
                    for (int k = 0; k < size; k++) {
                        cov[i][j] += (cdata[i][k] - mean[i]) * (cdata[j][k] - mean[j]);
                    }
                    cov[i][j] /= size;
                    cov[j][i] = cov[i][j];
                }
            }
            printMatrix(cov);
            Composition[] new_data = new Composition[SIZE];
            int windows = 1;
            for (int w = 0; w < windows; w++) {
                for (int i = 0; i < SIZE; i++) new_data[i] = random.nextGaussianBasis(mean, cov);
                TernaryPlotWindow frame2 = new TernaryPlotWindow(df, new TernaryPlotBuilder(names, new_data).build(), "TEST");
                frame2.setSize(600, 600);
                frame2.setVisible(true);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(testAdjustNormalDistribution.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(testAdjustNormalDistribution.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(testAdjustNormalDistribution.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
