    private static void solve_l1r_l2_svc(Problem prob_col, double[] w, double eps, double Cp, double Cn) {
        int l = prob_col.l;
        int w_size = prob_col.n;
        int j, s, iter = 0;
        int max_iter = 1000;
        int active_size = w_size;
        int max_num_linesearch = 20;
        double sigma = 0.01;
        double d, G_loss, G, H;
        double Gmax_old = Double.POSITIVE_INFINITY;
        double Gmax_new;
        double Gmax_init = 0;
        double d_old, d_diff;
        double loss_old = 0;
        double loss_new;
        double appxcond, cond;
        int[] index = new int[w_size];
        byte[] y = new byte[l];
        double[] b = new double[l];
        double[] xj_sq = new double[w_size];
        double[] C = new double[] { Cn, 0, Cp };
        for (j = 0; j < l; j++) {
            b[j] = 1;
            if (prob_col.y[j] > 0) y[j] = 1; else y[j] = -1;
        }
        for (j = 0; j < w_size; j++) {
            w[j] = 0;
            index[j] = j;
            xj_sq[j] = 0;
            for (FeatureNode xi : prob_col.x[j]) {
                int ind = xi.index - 1;
                double val = xi.value;
                xi.value *= y[ind];
                xj_sq[j] += C[GETI(y, ind)] * val * val;
            }
        }
        while (iter < max_iter) {
            Gmax_new = 0;
            for (j = 0; j < active_size; j++) {
                int i = j + random.nextInt(active_size - j);
                swap(index, i, j);
            }
            for (s = 0; s < active_size; s++) {
                j = index[s];
                G_loss = 0;
                H = 0;
                for (FeatureNode xi : prob_col.x[j]) {
                    int ind = xi.index - 1;
                    if (b[ind] > 0) {
                        double val = xi.value;
                        double tmp = C[GETI(y, ind)] * val;
                        G_loss -= tmp * b[ind];
                        H += tmp * val;
                    }
                }
                G_loss *= 2;
                G = G_loss;
                H *= 2;
                H = Math.max(H, 1e-12);
                double Gp = G + 1;
                double Gn = G - 1;
                double violation = 0;
                if (w[j] == 0) {
                    if (Gp < 0) violation = -Gp; else if (Gn > 0) violation = Gn; else if (Gp > Gmax_old / l && Gn < -Gmax_old / l) {
                        active_size--;
                        swap(index, s, active_size);
                        s--;
                        continue;
                    }
                } else if (w[j] > 0) violation = Math.abs(Gp); else violation = Math.abs(Gn);
                Gmax_new = Math.max(Gmax_new, violation);
                if (Gp <= H * w[j]) d = -Gp / H; else if (Gn >= H * w[j]) d = -Gn / H; else d = -w[j];
                if (Math.abs(d) < 1.0e-12) continue;
                double delta = Math.abs(w[j] + d) - Math.abs(w[j]) + G * d;
                d_old = 0;
                int num_linesearch;
                for (num_linesearch = 0; num_linesearch < max_num_linesearch; num_linesearch++) {
                    d_diff = d_old - d;
                    cond = Math.abs(w[j] + d) - Math.abs(w[j]) - sigma * delta;
                    appxcond = xj_sq[j] * d * d + G_loss * d + cond;
                    if (appxcond <= 0) {
                        for (FeatureNode x : prob_col.x[j]) {
                            b[x.index - 1] += d_diff * x.value;
                        }
                        break;
                    }
                    if (num_linesearch == 0) {
                        loss_old = 0;
                        loss_new = 0;
                        for (FeatureNode x : prob_col.x[j]) {
                            int ind = x.index - 1;
                            if (b[ind] > 0) {
                                loss_old += C[GETI(y, ind)] * b[ind] * b[ind];
                            }
                            double b_new = b[ind] + d_diff * x.value;
                            b[ind] = b_new;
                            if (b_new > 0) {
                                loss_new += C[GETI(y, ind)] * b_new * b_new;
                            }
                        }
                    } else {
                        loss_new = 0;
                        for (FeatureNode x : prob_col.x[j]) {
                            int ind = x.index - 1;
                            double b_new = b[ind] + d_diff * x.value;
                            b[ind] = b_new;
                            if (b_new > 0) {
                                loss_new += C[GETI(y, ind)] * b_new * b_new;
                            }
                        }
                    }
                    cond = cond + loss_new - loss_old;
                    if (cond <= 0) break; else {
                        d_old = d;
                        d *= 0.5;
                        delta *= 0.5;
                    }
                }
                w[j] += d;
                if (num_linesearch >= max_num_linesearch) {
                    info("#");
                    for (int i = 0; i < l; i++) b[i] = 1;
                    for (int i = 0; i < w_size; i++) {
                        if (w[i] == 0) continue;
                        for (FeatureNode x : prob_col.x[i]) {
                            b[x.index - 1] -= w[i] * x.value;
                        }
                    }
                }
            }
            if (iter == 0) Gmax_init = Gmax_new;
            iter++;
            if (iter % 10 == 0) info(".");
            if (Gmax_new <= eps * Gmax_init) {
                if (active_size == w_size) break; else {
                    active_size = w_size;
                    info("*");
                    Gmax_old = Double.POSITIVE_INFINITY;
                    continue;
                }
            }
            Gmax_old = Gmax_new;
        }
        info("%noptimization finished, #iter = %d%n", iter);
        if (iter >= max_iter) info("%nWARNING: reaching max number of iterations%n");
        double v = 0;
        int nnz = 0;
        for (j = 0; j < w_size; j++) {
            for (FeatureNode x : prob_col.x[j]) {
                x.value *= prob_col.y[x.index - 1];
            }
            if (w[j] != 0) {
                v += Math.abs(w[j]);
                nnz++;
            }
        }
        for (j = 0; j < l; j++) if (b[j] > 0) v += C[GETI(y, j)] * b[j] * b[j];
        info("Objective value = %f%n", v);
        info("#nonzeros/#features = %d/%d%n", nnz, w_size);
    }
