    public Matrice transposee() {
        final int ni = ni();
        final int nj = nj();
        final Matrice r = new Matrice(nj, ni);
        for (int i = 0; i < ni; i++) {
            for (int j = 0; j < nj; j++) {
                r.a_[j][i] = a_[i][j];
            }
        }
        return r;
    }
