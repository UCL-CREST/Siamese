    public void train(SampleSet sampleSet, StopHandle stopHandle) throws StopException, TrainException {
        log.info("initializing...");
        try {
            sampleSet.open();
            double[] x0 = (double[]) sampleSet.next().x;
            int l = x0.length;
            double[][] m = new double[l + 1][l + 1];
            double[] xy = new double[l + 1];
            sampleSet.first();
            while (sampleSet.hasNext()) {
                if (stopHandle != null && stopHandle.isStoped()) {
                    throw new StopException();
                }
                Sample sample = sampleSet.next();
                double[] x = (double[]) sample.x;
                double y = sample.y;
                double w = sample.weight;
                for (int i = 0; i < l; i++) {
                    for (int j = 0; j < i; j++) {
                        double xixj = x[i] * x[j];
                        m[i][j] += w * xixj;
                    }
                    m[i][i] += w * x[i] * x[i];
                    m[i][l] += w * x[i];
                    xy[i] += w * x[i] * y;
                }
                m[l][l] += w;
                xy[l] += w * y;
            }
            double q = 1.0d / m[l][l];
            for (int i = 0; i < l; i++) {
                for (int j = 0; j < i; j++) {
                    m[i][j] *= q;
                    m[j][i] = m[i][j];
                }
                m[i][i] = q * m[i][i] + lambda;
                m[i][l] *= q;
                m[l][i] = m[i][l];
                xy[i] *= q;
            }
            m[l][l] = 1;
            xy[l] *= q;
            Matrix M = new Matrix(m);
            Matrix XY = new Matrix(xy, xy.length);
            Matrix W = M.inverse().times(XY);
            weight = new double[l];
            for (int i = 0; i < l; i++) {
                weight[i] = W.get(i, 0);
            }
            bias = W.get(l, 0);
            squareNorm = computeSquareNorm();
            StringBuffer msg = new StringBuffer();
            msg.append("weight = ");
            for (int i = 0; i < l; i++) {
                if (i > 0) {
                    msg.append(",");
                }
                msg.append(weight[i]);
            }
            log.info(msg.toString());
            log.info("bias = " + bias);
        } catch (StopException e) {
            throw e;
        } catch (Exception e) {
            throw new TrainException(e);
        } finally {
            try {
                sampleSet.close();
            } catch (Exception e) {
                log.error(e);
            }
        }
    }
