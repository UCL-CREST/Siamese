    private static void solve_l2r_lr_dual(Problem prob, double w[], double eps, double Cp, double Cn) {
        int l = prob.l;
        int w_size = prob.n;
        int i, s, iter = 0;
        double xTx[] = new double[l];
        int max_iter = 1000;
        int index[] = new int[l];
        double alpha[] = new double[2 * l];
        byte y[] = new byte[l];
        int max_inner_iter = 100;
        double innereps = 1e-2;
        double innereps_min = Math.min(1e-8, eps);
        double upper_bound[] = new double[] { Cn, 0, Cp };
        for (i = 0; i < w_size; i++) w[i] = 0;
        for (i = 0; i < l; i++) {
            if (prob.y[i] > 0) {
                y[i] = +1;
            } else {
                y[i] = -1;
            }
            alpha[2 * i] = Math.min(0.001 * upper_bound[GETI(y, i)], 1e-8);
            alpha[2 * i + 1] = upper_bound[GETI(y, i)] - alpha[2 * i];
            xTx[i] = 0;
            for (FeatureNode xi : prob.x[i]) {
                xTx[i] += (xi.value) * (xi.value);
                w[xi.index - 1] += y[i] * alpha[2 * i] * xi.value;
            }
            index[i] = i;
        }
        while (iter < max_iter) {
            for (i = 0; i < l; i++) {
                int j = i + random.nextInt(l - i);
                swap(index, i, j);
            }
            int newton_iter = 0;
            double Gmax = 0;
            for (s = 0; s < l; s++) {
                i = index[s];
                byte yi = y[i];
                double C = upper_bound[GETI(y, i)];
                double ywTx = 0, xisq = xTx[i];
                for (FeatureNode xi : prob.x[i]) {
                    ywTx += w[xi.index - 1] * xi.value;
                }
                ywTx *= y[i];
                double a = xisq, b = ywTx;
                int ind1 = 2 * i, ind2 = 2 * i + 1, sign = 1;
                if (0.5 * a * (alpha[ind2] - alpha[ind1]) + b < 0) {
                    ind1 = 2 * i + 1;
                    ind2 = 2 * i;
                    sign = -1;
                }
                double alpha_old = alpha[ind1];
                double z = alpha_old;
                if (C - z < 0.5 * C) z = 0.1 * z;
                double gp = a * (z - alpha_old) + sign * b + Math.log(z / (C - z));
                Gmax = Math.max(Gmax, Math.abs(gp));
                final double eta = 0.1;
                int inner_iter = 0;
                while (inner_iter <= max_inner_iter) {
                    if (Math.abs(gp) < innereps) break;
                    double gpp = a + C / (C - z) / z;
                    double tmpz = z - gp / gpp;
                    if (tmpz <= 0) z *= eta; else z = tmpz;
                    gp = a * (z - alpha_old) + sign * b + Math.log(z / (C - z));
                    newton_iter++;
                    inner_iter++;
                }
                if (inner_iter > 0) {
                    alpha[ind1] = z;
                    alpha[ind2] = C - z;
                    for (FeatureNode xi : prob.x[i]) {
                        w[xi.index - 1] += sign * (z - alpha_old) * yi * xi.value;
                    }
                }
            }
            iter++;
            if (iter % 10 == 0) info(".");
            if (Gmax < eps) break;
            if (newton_iter < l / 10) innereps = Math.max(innereps_min, 0.1 * innereps);
        }
        info("%noptimization finished, #iter = %d%n", iter);
        if (iter >= max_iter) info("%nWARNING: reaching max number of iterations%nUsing -s 0 may be faster (also see FAQ)%n%n");
        double v = 0;
        for (i = 0; i < w_size; i++) v += w[i] * w[i];
        v *= 0.5;
        for (i = 0; i < l; i++) v += alpha[2 * i] * Math.log(alpha[2 * i]) + alpha[2 * i + 1] * Math.log(alpha[2 * i + 1]) - upper_bound[GETI(y, i)] * Math.log(upper_bound[GETI(y, i)]);
        info("Objective value = %f%n", v);
    }
