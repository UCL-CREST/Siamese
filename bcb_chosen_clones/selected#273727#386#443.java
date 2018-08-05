    void calcEllpsoid(double[] x) {
        double[][] p = new double[3][3];
        double[][] a = new double[3][3];
        double[][] tpa = new double[3][3];
        double[][] tp = new double[3][3];
        double sinphi, cosphi, sintheta, costheta, l02;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                a[i][j] = 0;
                tpa[i][j] = 0;
                ellipsoid[i][j] = 0;
            }
        }
        a[0][0] = 1.0 / ((l / 2) * (l / 2));
        a[1][1] = 1.0 / ((w / 2) * (w / 2));
        a[2][2] = 1.0 / ((d / 2) * (d / 2));
        l02 = Math.sqrt(x[0] * x[0] + x[2] * x[2]);
        if (x[0] > 0) {
            sinphi = x[1];
            cosphi = l02;
            sintheta = x[2] / l02;
            costheta = x[0] / l02;
        } else {
            sinphi = x[1];
            cosphi = -1.0 * l02;
            sintheta = -1.0 * x[2] / l02;
            costheta = -1.0 * x[0] / l02;
        }
        p[0][0] = cosphi * costheta;
        p[0][1] = sinphi;
        p[0][2] = cosphi * sintheta;
        p[1][0] = -1.0 * sinphi * costheta;
        p[1][1] = cosphi;
        p[1][2] = -1.0 * sinphi * sintheta;
        p[2][0] = -1.0 * sintheta;
        p[2][1] = 0;
        p[2][2] = costheta;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                tp[i][j] = p[j][i];
            }
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                for (int k = 0; k < 3; ++k) {
                    tpa[i][j] += tp[i][k] * a[k][j];
                }
            }
        }
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                for (int k = 0; k < 3; ++k) {
                    ellipsoid[i][j] += tpa[i][k] * p[k][j];
                }
            }
        }
        return;
    }
