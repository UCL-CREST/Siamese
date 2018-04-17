    public void bubble() {
        boolean test = false;
        int kars = 0, tas = 0;
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
        System.out.print(kars + " " + tas);
    }
