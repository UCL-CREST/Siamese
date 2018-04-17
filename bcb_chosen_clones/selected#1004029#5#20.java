    public static void main(String args[]) {
        int[] mas = { 3, 5, 6, 9, 1, -3, -4, -88 };
        int sort = 0;
        for (int j = 0; j < (mas.length); j++) {
            for (int i = 0; i < mas.length - 1; i++) {
                if (mas[i] > mas[i + 1]) {
                    sort = mas[i];
                    mas[i] = mas[i + 1];
                    mas[i + 1] = sort;
                }
            }
        }
        for (int i = 0; i < mas.length; i++) {
            System.out.print(" " + mas[i]);
        }
    }
