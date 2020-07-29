    public static void qrsolv_f77(int n, double r[][], int ipvt[], double diag[], double qtb[], double x[], double sdiag[], double wa[]) {
        int i, j, jp1, k, kp1, l, nsing;
        double cos, cotan, qtbpj, sin, sum, tan, temp;
        for (j = 1; j <= n; j++) {
            for (i = j; i <= n; i++) {
                r[i][j] = r[j][i];
            }
            x[j] = r[j][j];
            wa[j] = qtb[j];
        }
        for (j = 1; j <= n; j++) {
            l = ipvt[j];
            if (diag[l] != zero) {
                for (k = j; k <= n; k++) {
                    sdiag[k] = zero;
                }
                sdiag[j] = diag[l];
                qtbpj = zero;
                for (k = j; k <= n; k++) {
                    if (sdiag[k] != zero) {
                        if (Math.abs(r[k][k]) < Math.abs(sdiag[k])) {
                            cotan = r[k][k] / sdiag[k];
                            sin = p5 / Math.sqrt(p25 + p25 * cotan * cotan);
                            cos = sin * cotan;
                        } else {
                            tan = sdiag[k] / r[k][k];
                            cos = p5 / Math.sqrt(p25 + p25 * tan * tan);
                            sin = cos * tan;
                        }
                        r[k][k] = cos * r[k][k] + sin * sdiag[k];
                        temp = cos * wa[k] + sin * qtbpj;
                        qtbpj = -sin * wa[k] + cos * qtbpj;
                        wa[k] = temp;
                        kp1 = k + 1;
                        for (i = kp1; i <= n; i++) {
                            temp = cos * r[i][k] + sin * sdiag[i];
                            sdiag[i] = -sin * r[i][k] + cos * sdiag[i];
                            r[i][k] = temp;
                        }
                    }
                }
            }
            sdiag[j] = r[j][j];
            r[j][j] = x[j];
        }
        nsing = n;
        for (j = 1; j <= n; j++) {
            if (sdiag[j] == zero && nsing == n) nsing = j - 1;
            if (nsing < n) wa[j] = zero;
        }
        for (k = 1; k <= nsing; k++) {
            j = nsing - k + 1;
            sum = zero;
            jp1 = j + 1;
            for (i = jp1; i <= nsing; i++) {
                sum += r[i][j] * wa[i];
            }
            wa[j] = (wa[j] - sum) / sdiag[j];
        }
        for (j = 1; j <= n; j++) {
            l = ipvt[j];
            x[l] = wa[j];
        }
        return;
    }
