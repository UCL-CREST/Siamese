    public double[] calculateAngle() {
        double[] out = new double[3];
        rotation_matrix[0][0] = 1 - 2 * (q2 * q2 + q3 * q3);
        rotation_matrix[0][1] = 2 * (q1 * q2 + q3 * q4);
        rotation_matrix[0][2] = 2 * (q1 * q3 - q2 * q4);
        rotation_matrix[1][0] = 2 * (q1 * q2 - q3 * q4);
        rotation_matrix[1][1] = 1 - 2 * (q1 * q1 + q3 * q3);
        rotation_matrix[1][2] = 2 * (q2 * q3 + q1 * q4);
        rotation_matrix[2][0] = 2 * (q1 * q3 + q2 * q4);
        rotation_matrix[2][1] = 2 * (q2 * q3 - q1 * q4);
        rotation_matrix[2][2] = 1 - 2 * (q1 * q1 + q2 * q2);
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                element[j][i] = rotation_matrix[j][i];
                rotation_matrix[j][i] = rotation_matrix[i][j];
            }
        }
        double c_psi;
        double s_psi;
        double c_phi;
        double s_phi;
        Quad_check checkPsi;
        Quad_check checkPhi;
        theta = (-1) * Math.asin(element[0][2]) * 180 / Math.PI;
        c_psi = element[0][0] / Math.cos(theta * Math.PI / 180);
        s_psi = element[0][1] / Math.cos(theta * Math.PI / 180);
        System.out.println("c_psi=" + c_psi);
        System.out.println("s_psi=" + s_psi);
        checkPsi = new Quad_check(c_psi, s_psi);
        checkPsi.determineAngle();
        psi = checkPsi.angle;
        s_phi = element[1][2] / Math.cos(theta * Math.PI / 180);
        c_phi = element[2][2] / Math.cos(theta * Math.PI / 180);
        checkPhi = new Quad_check(c_phi, s_phi);
        checkPhi.determineAngle();
        phi = checkPhi.angle;
        out[0] = theta;
        out[1] = psi;
        out[2] = phi;
        return out;
    }
