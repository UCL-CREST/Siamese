    public void train(SampleSet sampleSet, StopHandle stopHandle) throws StopException, TrainException {
        try {
            log.info("initializing...");
            sampleSet.open();
            double[] x0 = (double[]) sampleSet.next().x;
            int l = x0.length;
            double[] m1 = new double[l];
            double[] m0 = new double[l];
            double w1 = 0;
            double w0 = 0;
            double[][] s1 = new double[l][l];
            double[][] s0 = new double[l][l];
            while (sampleSet.hasNext()) {
                if (stopHandle != null && stopHandle.isStoped()) {
                    throw new StopException();
                }
                Sample sample = sampleSet.next();
                double[] x = (double[]) sample.x;
                int y = (int) sample.y;
                double w = sample.weight;
                if (y > 0) {
                    w1 += w;
                    for (int i = 0; i < l; i++) {
                        m1[i] += w * x[i];
                    }
                    for (int i = 0; i < l; i++) {
                        for (int j = 0; j < i; j++) {
                            s1[i][j] += w * x[i] * x[j];
                        }
                        s1[i][i] += w * x[i] * x[i];
                    }
                } else {
                    w0 += w;
                    for (int i = 0; i < l; i++) {
                        m0[i] += w * x[i];
                    }
                    for (int i = 0; i < l; i++) {
                        for (int j = 0; j < i; j++) {
                            s0[i][j] += w * x[i] * x[j];
                        }
                        s0[i][i] += w * x[i] * x[i];
                    }
                }
            }
            double q1 = 1.0d / w1;
            double q0 = 1.0d / w0;
            double q = 1.0d / (w1 + w0);
            for (int i = 0; i < l; i++) {
                m1[i] *= q1;
                m0[i] *= q0;
            }
            for (int i = 0; i < l; i++) {
                for (int j = 0; j < i; j++) {
                    s1[i][j] *= q1;
                    s1[j][i] = s1[i][j];
                    s0[i][j] *= q0;
                    s0[j][i] = s0[i][j];
                }
                s1[i][i] *= q1;
                s0[i][i] *= q0;
            }
            double[][] s = new double[l][l];
            for (int i = 0; i < l; i++) {
                for (int j = 0; j < i; j++) {
                    s[i][j] = q * (w1 * s1[i][j] + w0 * s0[i][j] - w1 * m1[i] * m1[j] - w0 * m0[i] * m0[j]);
                    s[j][i] = s[i][j];
                }
                s[i][i] = q * (w1 * s1[i][i] + w0 * s0[i][i] - w1 * m1[i] * m1[i] - w0 * m0[i] * m0[i]) + lambda;
            }
            double[] m = new double[l];
            for (int i = 0; i < l; i++) {
                m[i] = m1[i] - m0[i];
            }
            log.info("solving...");
            Matrix S = new Matrix(s);
            Matrix M = new Matrix(m, m.length);
            Matrix W = S.inverse().times(M);
            weight = W.getRowPackedCopy();
            log.info("solved!");
            regularize(w1, w0, m1, m0, s1, s0);
            log.info("mean[+1]=" + positiveMean + " var[+1]=" + positiveVar);
            log.info("mean[-1]=" + negativeMean + " var[-1]=" + negativeVar);
            StringBuffer msg = new StringBuffer();
            msg.append("weight = ");
            for (int i = 0; i < l; i++) {
                if (i > 0) {
                    msg.append(",");
                }
                msg.append(weight[i]);
            }
            log.info(msg.toString());
            log.info("bias=" + bias);
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
