    public void align() {
        jama.Matrix tmp;
        this.cm1 = new Point3d();
        this.cm2 = new Point3d();
        this.cm1 = getCenterOfMass(p1, atwt1);
        this.cm2 = getCenterOfMass(p2, atwt2);
        for (int i = 0; i < this.npoint; i++) {
            p1[i].x = p1[i].x - this.cm1.x;
            p1[i].y = p1[i].y - this.cm1.y;
            p1[i].z = p1[i].z - this.cm1.z;
            p2[i].x = p2[i].x - this.cm2.x;
            p2[i].y = p2[i].y - this.cm2.y;
            p2[i].z = p2[i].z - this.cm2.z;
        }
        double[][] tR = new double[3][3];
        for (int i = 0; i < this.npoint; i++) {
            tR[0][0] += p1[i].x * p2[i].x * wts[i];
            tR[0][1] += p1[i].x * p2[i].y * wts[i];
            tR[0][2] += p1[i].x * p2[i].z * wts[i];
            tR[1][0] += p1[i].y * p2[i].x * wts[i];
            tR[1][1] += p1[i].y * p2[i].y * wts[i];
            tR[1][2] += p1[i].y * p2[i].z * wts[i];
            tR[2][0] += p1[i].z * p2[i].x * wts[i];
            tR[2][1] += p1[i].z * p2[i].y * wts[i];
            tR[2][2] += p1[i].z * p2[i].z * wts[i];
        }
        double[][] R = new double[3][3];
        tmp = new jama.Matrix(tR);
        R = tmp.transpose().getArray();
        double[][] RtR = new double[3][3];
        jama.Matrix jamaR = new jama.Matrix(R);
        tmp = tmp.times(jamaR);
        RtR = tmp.getArray();
        jama.Matrix jamaRtR = new jama.Matrix(RtR);
        jama.EigenvalueDecomposition ed = jamaRtR.eig();
        double[] mu = ed.getRealEigenvalues();
        double[][] a = ed.getV().getArray();
        double tmp2 = mu[2];
        mu[2] = mu[0];
        mu[0] = tmp2;
        for (int i = 0; i < 3; i++) {
            tmp2 = a[i][2];
            a[i][2] = a[i][0];
            a[i][0] = tmp2;
        }
        a[0][2] = (a[1][0] * a[2][1]) - (a[1][1] * a[2][0]);
        a[1][2] = (a[0][1] * a[2][0]) - (a[0][0] * a[2][1]);
        a[2][2] = (a[0][0] * a[1][1]) - (a[0][1] * a[1][0]);
        double[][] b = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    b[i][j] += R[i][k] * a[k][j];
                }
                b[i][j] = b[i][j] / Math.sqrt(mu[j]);
            }
        }
        double norm1 = 0.;
        double norm2 = 0.;
        for (int i = 0; i < 3; i++) {
            norm1 += b[i][0] * b[i][0];
            norm2 += b[i][1] * b[i][1];
        }
        norm1 = Math.sqrt(norm1);
        norm2 = Math.sqrt(norm2);
        for (int i = 0; i < 3; i++) {
            b[i][0] = b[i][0] / norm1;
            b[i][1] = b[i][1] / norm2;
        }
        b[0][2] = (b[1][0] * b[2][1]) - (b[1][1] * b[2][0]);
        b[1][2] = (b[0][1] * b[2][0]) - (b[0][0] * b[2][1]);
        b[2][2] = (b[0][0] * b[1][1]) - (b[0][1] * b[1][0]);
        double[][] tU = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    tU[i][j] += b[i][k] * a[j][k];
                }
            }
        }
        U = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                U[i][j] = tU[j][i];
            }
        }
        this.rp = new Point3d[this.npoint];
        for (int i = 0; i < this.npoint; i++) {
            this.rp[i] = new Point3d(U[0][0] * p2[i].x + U[0][1] * p2[i].y + U[0][2] * p2[i].z, U[1][0] * p2[i].x + U[1][1] * p2[i].y + U[1][2] * p2[i].z, U[2][0] * p2[i].x + U[2][1] * p2[i].y + U[2][2] * p2[i].z);
        }
        double rms = 0.;
        for (int i = 0; i < this.npoint; i++) {
            rms += (p1[i].x - this.rp[i].x) * (p1[i].x - this.rp[i].x) + (p1[i].y - this.rp[i].y) * (p1[i].y - this.rp[i].y) + (p1[i].z - this.rp[i].z) * (p1[i].z - this.rp[i].z);
        }
        this.rmsd = Math.sqrt(rms / this.npoint);
    }
