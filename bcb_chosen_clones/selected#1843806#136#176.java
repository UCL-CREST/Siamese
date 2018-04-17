    public static double[][] rotation(final double[][] pos, final double[][] refPos) {
        if (pos.length != refPos.length) throw new IllegalArgumentException("All arguments must have the same length.");
        double R_m[] = new double[3];
        double R_p[] = new double[3];
        double matrix[][] = new double[4][4];
        for (int k = 0; k < pos.length; ++k) {
            R_m = BLA.subtract(pos[k], refPos[k]);
            R_p = BLA.add(refPos[k], pos[k]);
            matrix[0][0] += (R_m[0] * R_m[0] + R_m[1] * R_m[1] + R_m[2] * R_m[2]);
            matrix[1][1] += (R_m[0] * R_m[0] + R_p[1] * R_p[1] + R_p[2] * R_p[2]);
            matrix[2][2] += (R_p[0] * R_p[0] + R_m[1] * R_m[1] + R_p[2] * R_p[2]);
            matrix[3][3] += (R_p[0] * R_p[0] + R_p[1] * R_p[1] + R_m[2] * R_m[2]);
            matrix[1][0] += (R_m[2] * R_p[1] - R_m[1] * R_p[2]);
            matrix[2][0] += (R_p[2] * R_m[0] - R_p[0] * R_m[2]);
            matrix[2][1] += (R_m[0] * R_m[1] - R_p[1] * R_p[0]);
            matrix[3][0] += (R_m[1] * R_p[0] - R_p[1] * R_m[0]);
            matrix[3][1] += (R_m[2] * R_m[0] - R_p[0] * R_p[2]);
            matrix[3][2] += (R_m[2] * R_m[1] - R_p[1] * R_p[2]);
        }
        for (int i = 0; i < 4; ++i) {
            for (int j = i + 1; j < 4; ++j) {
                matrix[i][j] = matrix[j][i];
            }
        }
        double eigenvals[] = new double[4];
        BLA.diagonalizeSymmetric(matrix, eigenvals);
        double q[] = new double[4];
        for (int i = 0; i < 4; ++i) q[i] = matrix[i][3];
        double operator[][] = BLA.zeroes(4, 4);
        operator[0][0] = q[0] * q[0] + q[1] * q[1] - q[2] * q[2] - q[3] * q[3];
        operator[1][1] = q[0] * q[0] + q[2] * q[2] - q[1] * q[1] - q[3] * q[3];
        operator[2][2] = q[0] * q[0] + q[3] * q[3] - q[1] * q[1] - q[2] * q[2];
        operator[1][0] = 2 * (q[1] * q[2] - q[0] * q[3]);
        operator[2][0] = 2 * (q[1] * q[3] + q[0] * q[2]);
        operator[2][1] = 2 * (q[2] * q[3] - q[0] * q[1]);
        operator[0][1] = 2 * (q[1] * q[2] + q[0] * q[3]);
        operator[0][2] = 2 * (q[1] * q[3] - q[0] * q[2]);
        operator[1][2] = 2 * (q[2] * q[3] + q[0] * q[1]);
        operator[3] = eigenvals;
        return operator;
    }
