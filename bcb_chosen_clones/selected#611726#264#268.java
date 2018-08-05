    public static byte[][] transpose(byte[][] m) {
        byte[][] n = new byte[m[0].length][m.length];
        for (int j = 0; j < m.length; j++) for (int i = 0; i < m[0].length; i++) n[i][j] = m[j][i];
        return n;
    }
