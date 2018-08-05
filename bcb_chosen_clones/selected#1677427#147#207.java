    protected static double[] computeMaCoef(double[] arCoef, int fakeArLevel, int maLevel, Sample[] samples, StopHandle handle) throws StopException, TrainException {
        if (maLevel > 0) {
            ARMA arma = new ARMA(fakeArLevel, 0);
            Sample[] maSamples = new Sample[samples.length];
            for (int i = 0; i < samples.length; i++) {
                double[] seq = (double[]) samples[i].x;
                double w = samples[i].weight;
                double[] x = getMaseq(arCoef, seq);
                maSamples[i] = new Sample(x, 0);
                maSamples[i].weight = w;
            }
            arma.train(new ArraySampleSet(maSamples), handle);
            double[][] m = new double[maLevel][maLevel];
            double[] xy = new double[maLevel];
            double total = 0;
            for (int i = 0; i < maSamples.length; i++) {
                double w = maSamples[i].weight;
                double[] maseq = (double[]) maSamples[i].x;
                double[] eseq = getMaseq(arma.arCoef, maseq);
                for (int j = maLevel + fakeArLevel; j < eseq.length; j++) {
                    double y = maseq[j];
                    double[] x = new double[maLevel];
                    System.arraycopy(eseq, j - fakeArLevel - maLevel, x, 0, maLevel);
                    total += w;
                    for (int p = 0; p < maLevel; p++) {
                        for (int q = 0; q < p; q++) {
                            double xpxq = x[p] * x[q];
                            m[p][q] += w * xpxq;
                        }
                        m[p][p] += w * x[p] * x[p];
                        xy[p] += w * x[p] * y;
                    }
                }
            }
            for (int i = 0; i < maLevel; i++) {
                for (int j = 0; j < i; j++) {
                    m[i][j] /= total;
                    m[j][i] = m[i][j];
                }
                m[i][i] /= total;
                xy[i] /= total;
            }
            Matrix M = new Matrix(m);
            Matrix XY = new Matrix(xy, xy.length);
            Matrix W = null;
            try {
                W = M.inverse().times(XY);
            } catch (Exception e) {
                W = M.pseudoinverse().times(XY);
            }
            double[] weight = W.getRowPackedCopy();
            double[] coef = new double[maLevel + 1];
            coef[0] = 1;
            for (int i = 1; i < coef.length; i++) {
                coef[i] = weight[weight.length - i];
            }
            return coef;
        } else {
            return new double[] { 1 };
        }
    }
