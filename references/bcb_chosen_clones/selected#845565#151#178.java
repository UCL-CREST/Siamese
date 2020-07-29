    private void transform() {
        double apq = rows[p][q];
        if (apq == 0) return;
        double app = rows[p][p];
        double aqq = rows[q][q];
        double arp = (aqq - app) * 0.5 / apq;
        double t = arp > 0 ? 1 / (Math.sqrt(arp * arp + 1) + arp) : 1 / (arp - Math.sqrt(arp * arp + 1));
        double c = 1 / Math.sqrt(t * t + 1);
        double s = t * c;
        double tau = s / (1 + c);
        rows[p][p] = app - t * apq;
        rows[q][q] = aqq + t * apq;
        rows[p][q] = 0;
        rows[q][p] = 0;
        int n = rows.length;
        for (int i = 0; i < n; i++) {
            if (i != p && i != q) {
                rows[p][i] = rows[i][p] - s * (rows[i][q] + tau * rows[i][p]);
                rows[q][i] = rows[i][q] + s * (rows[i][p] - tau * rows[i][q]);
                rows[i][p] = rows[p][i];
                rows[i][q] = rows[q][i];
            }
            arp = transform[i][p];
            aqq = transform[i][q];
            transform[i][p] = arp - s * (aqq + tau * arp);
            transform[i][q] = aqq + s * (arp - tau * aqq);
        }
    }
