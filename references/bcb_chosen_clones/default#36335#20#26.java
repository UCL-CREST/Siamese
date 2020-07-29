    protected void permute(Sequence s) {
        Random rnd = new java.util.Random();
        for (int i = s.size() - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            if (j < i) s.swapElements(s.atRank(i), s.atRank(j));
        }
    }
