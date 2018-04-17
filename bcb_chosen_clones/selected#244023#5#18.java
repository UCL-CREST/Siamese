    public static void main(String[] args) {
        int[] mas = { 5, 10, 20, -30, 55, -60, 9, -40, -20 };
        int next;
        for (int a = 0; a < mas.length; a++) {
            for (int i = 0; i < mas.length - 1; i++) {
                if (mas[i] > mas[i + 1]) {
                    next = mas[i];
                    mas[i] = mas[i + 1];
                    mas[i + 1] = next;
                }
            }
        }
        for (int i = 0; i < mas.length; i++) System.out.print(" " + mas[i]);
    }
