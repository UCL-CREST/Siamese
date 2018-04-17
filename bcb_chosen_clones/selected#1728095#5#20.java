    public static void main(String args[]) {
        int temp;
        int[] a1 = { 6, 2, -3, 7, -1, 8, 9, 0 };
        for (int j = 0; j < (a1.length * a1.length); j++) {
            for (int i = 0; i < a1.length - 1; i++) {
                if (a1[i] > a1[i + 1]) {
                    temp = a1[i];
                    a1[i] = a1[i + 1];
                    a1[i + 1] = temp;
                }
            }
        }
        for (int i = 0; i < a1.length; i++) {
            System.out.print(" " + a1[i]);
        }
    }
