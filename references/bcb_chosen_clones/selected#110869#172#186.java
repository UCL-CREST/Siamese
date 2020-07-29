    public void method31() {
        boolean flag = true;
        while (flag) {
            flag = false;
            for (int i = 0; i < anInt772 - 1; i++) if (anIntArray774[i] < anIntArray774[i + 1]) {
                int j = anIntArray774[i];
                anIntArray774[i] = anIntArray774[i + 1];
                anIntArray774[i + 1] = j;
                long l = aLongArray773[i];
                aLongArray773[i] = aLongArray773[i + 1];
                aLongArray773[i + 1] = l;
                flag = true;
            }
        }
    }
