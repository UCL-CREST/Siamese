    public int distance(int n1, int n2) {
        if (invalid[n1]) {
            for (int i = 0; i < nodeCount; i++) {
                distances[n1][i] = 0;
                distances[i][n1] = 0;
            }
            invalid[n1] = false;
        }
        if (invalid[n2]) {
            for (int i = 0; i < nodeCount; i++) {
                distances[n2][i] = 0;
                distances[i][n2] = 0;
            }
            invalid[n2] = false;
        }
        if (distances[n1][n2] == 0) {
            long x = xs[n1] - xs[n2];
            long y = ys[n1] - ys[n2];
            distances[n1][n2] = (int) Math.sqrt(x * x + y * y);
            distances[n2][n1] = distances[n1][n2];
        }
        return distances[n1][n2];
    }
