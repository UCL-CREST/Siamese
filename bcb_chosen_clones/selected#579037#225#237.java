    void bubbleSort(int ids[]) {
        boolean flag = true;
        int temp;
        while (flag) {
            flag = false;
            for (int i = 0; i < ids.length - 1; i++) if (ids[i] < ids[i + 1]) {
                temp = ids[i];
                ids[i] = ids[i + 1];
                ids[i + 1] = temp;
                flag = true;
            }
        }
    }
