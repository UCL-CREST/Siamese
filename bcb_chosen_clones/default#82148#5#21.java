    public static void main(String args[]) {
        int i, j, l;
        short NUMNUMBERS = 256;
        short numbers[] = new short[NUMNUMBERS];
        Darjeeling.print("START");
        for (l = 0; l < 100; l++) {
            for (i = 0; i < NUMNUMBERS; i++) numbers[i] = (short) (NUMNUMBERS - 1 - i);
            for (i = 0; i < NUMNUMBERS; i++) {
                for (j = 0; j < NUMNUMBERS - i - 1; j++) if (numbers[j] > numbers[j + 1]) {
                    short temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                }
            }
        }
        Darjeeling.print("END");
    }
