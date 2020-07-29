    private static void solve_l2r_l1l2_svc(Problem prob, double[] w, double eps, double Cp, double Cn, SolverType solver_type) {
        int l = prob.l;
        int w_size = prob.n;
        int i, s, iter = 0;
        double C, d, G;
        double[] QD = new double[l];
        int max_iter = 1000;
        int[] index = new int[l];
        double[] alpha = new double[l];
        byte[] y = new byte[l];
        int active_size = l;
        double PG;
        double PGmax_old = Double.POSITIVE_INFINITY;
        double PGmin_old = Double.NEGATIVE_INFINITY;
        double PGmax_new, PGmin_new;
        double diag[] = new double[] { 0.5 / Cn, 0, 0.5 / Cp };
        double upper_bound[] = new double[] { Double.POSITIVE_INFINITY, 0, Double.POSITIVE_INFINITY };
        if (solver_type == SolverType.L2R_L1LOSS_SVC_DUAL) {
            diag[0] = 0;
            diag[2] = 0;
            upper_bound[0] = Cn;
            upper_bound[2] = Cp;
        }
        for (i = 0; i < w_size; i++) w[i] = 0;
        for (i = 0; i < l; i++) {
            alpha[i] = 0;
            if (prob.y[i] > 0) {
                y[i] = +1;
            } else {
                y[i] = -1;
            }
            QD[i] = diag[GETI(y, i)];
            for (FeatureNode xi : prob.x[i]) {
                QD[i] += xi.value * xi.value;
            }
            index[i] = i;
        }
        while (iter < max_iter) {
            PGmax_new = Double.NEGATIVE_INFINITY;
            PGmin_new = Double.POSITIVE_INFINITY;
            for (i = 0; i < active_size; i++) {
                int j = i + random.nextInt(active_size - i);
                swap(index, i, j);
            }
            for (s = 0; s < active_size; s++) {
                i = index[s];
                G = 0;
                byte yi = y[i];
                for (FeatureNode xi : prob.x[i]) {
                    G += w[xi.index - 1] * xi.value;
                }
                G = G * yi - 1;
                C = upper_bound[GETI(y, i)];
                G += alpha[i] * diag[GETI(y, i)];
                PG = 0;
                if (alpha[i] == 0) {
                    if (G > PGmax_old) {
                        active_size--;
                        swap(index, s, active_size);
                        s--;
                        continue;
                    } else if (G < 0) {
                        PG = G;
                    }
                } else if (alpha[i] == C) {
                    if (G < PGmin_old) {
                        active_size--;
                        swap(index, s, active_size);
                        s--;
                        continue;
                    } else if (G > 0) {
                        PG = G;
                    }
                } else {
                    PG = G;
                }
                PGmax_new = Math.max(PGmax_new, PG);
                PGmin_new = Math.min(PGmin_new, PG);
                if (Math.abs(PG) > 1.0e-12) {
                    double alpha_old = alpha[i];
                    alpha[i] = Math.min(Math.max(alpha[i] - G / QD[i], 0.0), C);
                    d = (alpha[i] - alpha_old) * yi;
                    for (FeatureNode xi : prob.x[i]) {
                        w[xi.index - 1] += d * xi.value;
                    }
                }
            }
            iter++;
            if (iter % 10 == 0) info(".");
            if (PGmax_new - PGmin_new <= eps) {
                if (active_size == l) break; else {
                    active_size = l;
                    info("*");
                    PGmax_old = Double.POSITIVE_INFINITY;
                    PGmin_old = Double.NEGATIVE_INFINITY;
                    continue;
                }
            }
            PGmax_old = PGmax_new;
            PGmin_old = PGmin_new;
            if (PGmax_old <= 0) PGmax_old = Double.POSITIVE_INFINITY;
            if (PGmin_old >= 0) PGmin_old = Double.NEGATIVE_INFINITY;
        }
        info(NL + "optimization finished, #iter = %d" + NL, iter);
        if (iter >= max_iter) info("%nWARNING: reaching max number of iterations%nUsing -s 2 may be faster (also see FAQ)%n%n");
        double v = 0;
        int nSV = 0;
        for (i = 0; i < w_size; i++) v += w[i] * w[i];
        for (i = 0; i < l; i++) {
            v += alpha[i] * (alpha[i] * diag[GETI(y, i)] - 2);
            if (alpha[i] > 0) ++nSV;
        }
        info("Objective value = %f" + NL, v / 2);
        info("nSV = %d" + NL, nSV);
    }
