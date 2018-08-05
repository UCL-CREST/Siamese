    public static void sort(float norm_abst[]) {
        float temp;
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                if (norm_abst[j] > norm_abst[j + 1]) {
                    temp = norm_abst[j];
                    norm_abst[j] = norm_abst[j + 1];
                    norm_abst[j + 1] = temp;
                }
            }
        }
        printFixed(norm_abst[0]);
        print(" ");
        printFixed(norm_abst[1]);
        print(" ");
        printFixed(norm_abst[2]);
        print(" ");
        printFixed(norm_abst[3]);
        print(" ");
        printFixed(norm_abst[4]);
        print(" ");
        printFixed(norm_abst[5]);
        print(" ");
        printFixed(norm_abst[6]);
        print(" ");
        printFixed(norm_abst[7]);
        print("\n");
    }
