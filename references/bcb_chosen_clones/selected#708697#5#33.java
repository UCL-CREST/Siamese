    public static void main(String[] args) {
        int dizi[] = { 23, 78, 45, 8, 3, 32, 56, 39, 92, 28 };
        boolean test = false;
        int kars = 0;
        int tas = 0;
        while (true) {
            for (int j = 0; j < dizi.length - 1; j++) {
                kars++;
                if (dizi[j] > dizi[j + 1]) {
                    int temp = dizi[j];
                    dizi[j] = dizi[j + 1];
                    dizi[j + 1] = temp;
                    test = true;
                    tas++;
                }
            }
            if (!test) {
                break;
            } else {
                test = false;
            }
        }
        for (int i = 0; i < dizi.length; i++) {
            System.out.print(dizi[i] + " ");
        }
        for (int i = 0; i < 5; i++) {
            System.out.println("i" + i);
        }
    }
