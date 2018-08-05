    public static void LU(FullMatrix A, FullMatrix L, FullMatrix U, SparseMatrix P) {
        FullMatrix AA = A.copy();
        int N = AA.getRowDim();
        int[] VP = new int[N];
        for (int n = 0; n < N; n++) {
            VP[n] = n;
        }
        double[][] dA = AA.getData();
        double[][] dL = L.getData();
        double[][] dU = U.getData();
        double[][] dUT = new double[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                dUT[j][i] = dU[i][j];
            }
        }
        for (int n = 0; n < N; n++) {
            boolean bFind = false;
            for (int nn = n; nn < N; nn++) {
                double sum1 = 0.0;
                double[] _dUTnn = dUT[nn];
                double[] _dLnn = dL[nn];
                for (int k = 0; k <= n - 1; k++) {
                    sum1 += _dLnn[k] * _dUTnn[k];
                }
                double ann = dA[nn][n];
                if (Math.abs(ann - sum1) > eps) {
                    if (nn != n) {
                        int tmp = VP[nn];
                        VP[nn] = VP[n];
                        VP[n] = tmp;
                        double vtmp;
                        for (int c = 0; c < N; c++) {
                            vtmp = dA[n][c];
                            dA[n][c] = dA[nn][c];
                            dA[nn][c] = vtmp;
                        }
                        for (int c = 0; c < n; c++) {
                            vtmp = dL[n][c];
                            dL[n][c] = dL[nn][c];
                            dL[nn][c] = vtmp;
                        }
                    }
                    bFind = true;
                    break;
                }
            }
            if (!bFind) throw new FutureyeException("Matrix is singular!");
            double[] _dAn = dA[n];
            double[] _dLn = dL[n];
            for (int j = n; j < N; j++) {
                double sum1 = 0.0;
                double[] _dUTj = dUT[j];
                for (int k = 0; k <= n - 1; k++) {
                    sum1 += _dLn[k] * _dUTj[k];
                }
                double anj = _dAn[j];
                dUT[j][n] = anj - sum1;
            }
            for (int i = n + 1; i < N; i++) {
                double sum2 = 0.0;
                double[] _dLi = dL[i];
                double[] _dUTn = dUT[n];
                for (int k = 0; k <= n - 1; k++) {
                    sum2 += _dLi[k] * _dUTn[k];
                }
                double ain = dA[i][n];
                dL[i][n] = (ain - sum2) / dUT[n][n];
            }
            dL[n][n] = 1.0;
        }
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                dU[i][j] = dUT[j][i];
            }
        }
        for (int i = 1; i <= N; i++) {
            P.set(i, VP[i - 1] + 1, 1.0);
        }
    }
