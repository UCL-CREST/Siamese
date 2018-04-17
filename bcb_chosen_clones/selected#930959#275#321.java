        void assignAct(boolean[][] pos, Map<String, Integer> controllerCount) {
            h = new Histogram[pos.length];
            modExpr = new double[pos.length];
            activity = new double[pos.length];
            for (int i = 0; i < pos.length; i++) {
                modExpr[i] = CellTypeMatcher.getMeanValue(mod, pos[i]);
                double total = 0;
                double act = 0;
                for (Triplet t : trips) {
                    double mv = CellTypeMatcher.getMeanValue(t.T, pos[i]);
                    if (Double.isNaN(mv)) continue;
                    act += Math.signum(Difference.calcGamma(t)) * mv / controllerCount.get(t.target);
                    total += 1D / controllerCount.get(t.target);
                }
                activity[i] = total == 0 ? Double.NaN : act / total;
            }
            expChPval = new double[pos.length][pos.length];
            for (int i = 0; i < pos.length; i++) {
                for (int j = i; j < pos.length; j++) {
                    if (j == i) expChPval[i][j] = 1; else {
                        expChPval[i][j] = CellTypeMatcher.getChangePvalBetweenTissues(mod, pos[i], pos[j]);
                        expChPval[j][i] = expChPval[i][j];
                    }
                }
            }
            actChPval = new double[pos.length][pos.length];
            for (int i = 0; i < pos.length; i++) {
                for (int j = i; j < pos.length; j++) {
                    if (j == i) actChPval[i][j] = 1; else {
                        int total = 0;
                        int samesign = 0;
                        for (Triplet t : trips) {
                            if (CellTypeMatcher.getChangePvalBetweenTissues(t.T, pos[i], pos[j]) < 0.05) {
                                double v1 = CellTypeMatcher.getMeanValue(t.T, pos[i]);
                                double v2 = CellTypeMatcher.getMeanValue(t.T, pos[j]);
                                if (Math.signum(Difference.calcGamma(t)) == Math.signum(v2 - v1)) {
                                    samesign++;
                                }
                                total++;
                            }
                        }
                        if (total == 0) actChPval[i][j] = 1; else actChPval[i][j] = Binomial.getPval(samesign, total - samesign);
                        actChPval[j][i] = actChPval[i][j];
                    }
                }
            }
        }
