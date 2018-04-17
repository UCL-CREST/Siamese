    public static void symmetrizeComponents(double[][] components) {
        for (int i = 0; i < components.length; i++) {
            for (int j = i + 1; j < components.length; j++) {
                components[i][j] += components[j][i];
                components[i][j] *= 0.5;
                components[j][i] = components[i][j];
            }
        }
    }
