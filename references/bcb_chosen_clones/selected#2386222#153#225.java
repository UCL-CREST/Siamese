    public void optimize() {
        System.arraycopy(x, 0, xNew, 0, dimension);
        lineSearchMethod.minimize(xNew, direction);
        for (int i = 0; i < dimension; i++) xDelta[i] = xNew[i] - x[i];
        System.arraycopy(xNew, 0, x, 0, dimension);
        System.arraycopy(g, 0, y, 0, dimension);
        g = function.gradientAt(Point.at(x)).toArray();
        for (int i = 0; i < dimension; i++) y[i] = g[i] - y[i];
        for (int i = 0; i < dimension; i++) {
            Hy[i] = 0.0;
            for (int j = 0; j < dimension; j++) Hy[i] += H[i][j] * y[j];
        }
        fac = 0.0;
        fae = 0.0;
        sumY = 0.0;
        sumXDelta = 0.0;
        for (int i = 0; i < dimension; i++) {
            fac += y[i] * xDelta[i];
            fae += y[i] * Hy[i];
            sumY += y[i] * y[i];
            sumXDelta += xDelta[i] * xDelta[i];
        }
        if (fac > Math.sqrt(MachineAccuracy.EPSILON * sumY * sumXDelta)) {
            fac = 1.0 / fac;
            fad = 1.0 / fae;
            switch(updateMethod) {
                case DFP:
                    for (int i = 0; i < dimension; i++) for (int j = i; j < dimension; j++) {
                        H[i][j] += fac * xDelta[i] * xDelta[j] - fad * Hy[i] * Hy[j];
                        H[j][i] = H[i][j];
                    }
                    break;
                case BFGS:
                    for (int i = 0; i < dimension; i++) y[i] = fac * xDelta[i] - fad * Hy[i];
                    for (int i = 0; i < dimension; i++) for (int j = i; j < dimension; j++) {
                        H[i][j] += fac * xDelta[i] * xDelta[j] - fad * Hy[i] * Hy[j] + fae * y[i] * y[j];
                        H[j][i] = H[i][j];
                    }
                    break;
                case BROYDEN_FAMILY:
                    for (int i = 0; i < dimension; i++) y[i] = fac * xDelta[i] - fad * Hy[i];
                    for (int i = 0; i < dimension; i++) for (int j = i; j < dimension; j++) {
                        H[i][j] += fac * xDelta[i] * xDelta[j] - fad * Hy[i] * Hy[j] + phi * (fae * y[i] * y[j]);
                        H[j][i] = H[i][j];
                    }
                    break;
                case BROYDEN:
                    fae = 0.0;
                    for (int i = 0; i < dimension; i++) {
                        xH[i] = 0.0;
                        for (int j = 0; j < dimension; j++) xH[i] += xDelta[j] * H[j][i];
                        fae += xDelta[i] * Hy[i];
                    }
                    fae = 1.0 / fae;
                    for (int i = 0; i < dimension; i++) {
                        y[i] = xDelta[i] - Hy[i];
                        for (int j = i; j < dimension; j++) {
                            H[i][j] += fae * y[i] * xH[j];
                            H[j][i] = H[i][j];
                        }
                    }
                    break;
            }
        }
        for (int i = 0; i < dimension; i++) {
            direction[i] = 0.0;
            for (int j = 0; j < dimension; j++) direction[i] -= H[i][j] * g[j];
        }
        solution = ValuePoint.at(Point.at(x), function);
        telemetry = new ValuePointTelemetry(solution);
        if (consumer != null) consumer.notifyOf(this);
        stopCondition.setValue(solution.getValue());
    }
