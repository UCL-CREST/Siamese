    public void optimize(MultivariateFunction f, double[] xvector, double tolfx, double tolx, MinimiserMonitor monitor) {
        t = tolx;
        fun = f;
        x = xvector;
        checkBounds(x);
        h = step;
        dim = fun.getNumArguments();
        d = new double[dim];
        y = new double[dim];
        z = new double[dim];
        q0 = new double[dim];
        q1 = new double[dim];
        v = new double[dim][dim];
        tflin = new double[dim];
        small = MachineAccuracy.EPSILON * MachineAccuracy.EPSILON;
        vsmall = small * small;
        large = 1.0 / small;
        vlarge = 1.0 / vsmall;
        ldfac = (illc ? 0.1 : 0.01);
        nl = kt = 0;
        numFun = 1;
        fx = fun.evaluate(x);
        stopCondition(fx, x, tolfx, tolx, true);
        qf1 = fx;
        t2 = small + Math.abs(t);
        t = t2;
        dmin = small;
        if (h < 100.0 * t) h = 100.0 * t;
        ldt = h;
        for (i = 0; i < dim; i++) {
            for (j = 0; j < dim; j++) {
                v[i][j] = (i == j ? 1.0 : 0.0);
            }
        }
        d[0] = 0.0;
        qd0 = 0.0;
        for (i = 0; i < dim; i++) q1[i] = x[i];
        if (prin > 1) {
            System.out.println("\n------------- enter function praxis -----------\n");
            System.out.println("... current parameter settings ...");
            System.out.println("... scaling ... " + scbd);
            System.out.println("...   tolx  ... " + t);
            System.out.println("...  tolfx  ... " + tolfx);
            System.out.println("... maxstep ... " + h);
            System.out.println("...   illc  ... " + illc);
            System.out.println("... maxFun  ... " + maxFun);
        }
        if (prin > 0) System.out.println();
        while (true) {
            sf = d[0];
            s = d[0] = 0.0;
            min1 = d[0];
            min2 = s;
            min(0, 2, fx, false);
            d[0] = min1;
            s = min2;
            if (s <= 0.0) for (i = 0; i < dim; i++) {
                v[i][0] = -v[i][0];
            }
            if ((sf <= (0.9 * d[0])) || ((0.9 * sf) >= d[0])) for (i = 1; i < dim; i++) d[i] = 0.0;
            boolean gotoFret = false;
            for (k = 1; k < dim; k++) {
                for (i = 0; i < dim; i++) {
                    y[i] = x[i];
                }
                sf = fx;
                illc = illc || (kt > 0);
                boolean gotoNext;
                do {
                    kl = k;
                    df = 0.0;
                    if (illc) {
                        for (i = 0; i < dim; i++) {
                            z[i] = (0.1 * ldt + t2 * Math.pow(10.0, (double) kt)) * (MathUtils.nextDouble() - 0.5);
                            s = z[i];
                            for (j = 0; j < dim; j++) {
                                x[j] += s * v[j][i];
                            }
                        }
                        checkBounds(x);
                        fx = fun.evaluate(x);
                        numFun++;
                    }
                    for (k2 = k; k2 < dim; k2++) {
                        sl = fx;
                        s = 0.0;
                        min1 = d[k2];
                        min2 = s;
                        min(k2, 2, fx, false);
                        d[k2] = min1;
                        s = min2;
                        if (illc) {
                            double szk = s + z[k2];
                            s = d[k2] * szk * szk;
                        } else s = sl - fx;
                        if (df < s) {
                            df = s;
                            kl = k2;
                        }
                    }
                    if (!illc && (df < Math.abs(100.0 * MachineAccuracy.EPSILON * fx))) {
                        illc = true;
                        gotoNext = true;
                    } else gotoNext = false;
                } while (gotoNext);
                if ((k == 1) && (prin > 1)) vecprint("\n... New Direction ...", d);
                for (k2 = 0; k2 <= k - 1; k2++) {
                    s = 0.0;
                    min1 = d[k2];
                    min2 = s;
                    min(k2, 2, fx, false);
                    d[k2] = min1;
                    s = min2;
                }
                f1 = fx;
                fx = sf;
                lds = 0.0;
                for (i = 0; i < dim; i++) {
                    sl = x[i];
                    x[i] = y[i];
                    y[i] = sl - y[i];
                    sl = y[i];
                    lds = lds + sl * sl;
                }
                checkBounds(x);
                lds = Math.sqrt(lds);
                if (lds > small) {
                    for (i = kl - 1; i >= k; i--) {
                        for (j = 0; j < dim; j++) v[j][i + 1] = v[j][i];
                        d[i + 1] = d[i];
                    }
                    d[k] = 0.0;
                    for (i = 0; i < dim; i++) v[i][k] = y[i] / lds;
                    min1 = d[k];
                    min2 = lds;
                    min(k, 4, f1, true);
                    d[k] = min1;
                    lds = min2;
                    if (lds <= 0.0) {
                        lds = -lds;
                        for (i = 0; i < dim; i++) v[i][k] = -v[i][k];
                    }
                }
                ldt = ldfac * ldt;
                if (ldt < lds) ldt = lds;
                if (prin > 1) print();
                if (monitor != null) {
                    monitor.newMinimum(fx, x);
                }
                if (stopCondition(fx, x, tolfx, tolx, false)) {
                    kt++;
                } else {
                    kt = 0;
                }
                if (kt > 1) {
                    gotoFret = true;
                    break;
                }
            }
            if (gotoFret) break;
            quadr();
            dn = 0.0;
            for (i = 0; i < dim; i++) {
                d[i] = 1.0 / Math.sqrt(d[i]);
                if (dn < d[i]) dn = d[i];
            }
            if (prin > 2) matprint("\n... New Matrix of Directions ...", v);
            for (j = 0; j < dim; j++) {
                s = d[j] / dn;
                for (i = 0; i < dim; i++) v[i][j] *= s;
            }
            if (scbd > 1.0) {
                s = vlarge;
                for (i = 0; i < dim; i++) {
                    sl = 0.0;
                    for (j = 0; j < dim; j++) sl += v[i][j] * v[i][j];
                    z[i] = Math.sqrt(sl);
                    if (z[i] < MachineAccuracy.SQRT_SQRT_EPSILON) z[i] = MachineAccuracy.SQRT_SQRT_EPSILON;
                    if (s > z[i]) s = z[i];
                }
                for (i = 0; i < dim; i++) {
                    sl = s / z[i];
                    z[i] = 1.0 / sl;
                    if (z[i] > scbd) {
                        sl = 1.0 / scbd;
                        z[i] = scbd;
                    }
                }
            }
            for (i = 1; i < dim; i++) for (j = 0; j <= i - 1; j++) {
                s = v[i][j];
                v[i][j] = v[j][i];
                v[j][i] = s;
            }
            minfit(dim, MachineAccuracy.EPSILON, vsmall, v, d);
            if (scbd > 1.0) {
                for (i = 0; i < dim; i++) {
                    s = z[i];
                    for (j = 0; j < dim; j++) v[i][j] *= s;
                }
                for (i = 0; i < dim; i++) {
                    s = 0.0;
                    for (j = 0; j < dim; j++) s += v[j][i] * v[j][i];
                    s = Math.sqrt(s);
                    d[i] *= s;
                    s = 1.0 / s;
                    for (j = 0; j < dim; j++) v[j][i] *= s;
                }
            }
            for (i = 0; i < dim; i++) {
                if ((dn * d[i]) > large) d[i] = vsmall; else if ((dn * d[i]) < small) d[i] = vlarge; else d[i] = Math.pow(dn * d[i], -2.0);
            }
            sort();
            dmin = d[dim - 1];
            if (dmin < small) dmin = small;
            illc = (MachineAccuracy.SQRT_EPSILON * d[0]) > dmin;
            if ((prin > 2) && (scbd > 1.0)) vecprint("\n... Scale Factors ...", z);
            if (prin > 2) vecprint("\n... Eigenvalues of A ...", d);
            if (prin > 2) matprint("\n... Eigenvectors of A ...", v);
            if ((maxFun > 0) && (nl > maxFun)) {
                if (prin > 0) System.out.println("\n... maximum number of function calls reached ...");
                break;
            }
        }
        if (prin > 0) {
            vecprint("\n... Final solution is ...", x);
            System.out.println("\n... Function value reduced to " + fx + " ...");
            System.out.println("... after " + numFun + " function calls.");
        }
    }
