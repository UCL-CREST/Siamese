    public void sort(int[] order, double[] values) {
        int temp = 0;
        boolean done = false;
        for (int i = 0; i < values.length; i++) {
            order[i] = i;
        }
        if (desendingValues) {
            while (!done) {
                done = true;
                for (int i = values.length - 2; i >= 0; i--) {
                    if (values[order[i]] < values[order[i + 1]]) {
                        done = false;
                        temp = order[i];
                        order[i] = order[i + 1];
                        order[i + 1] = temp;
                    }
                }
            }
        } else {
            while (!done) {
                done = true;
                for (int i = values.length - 2; i >= 0; i--) {
                    if (values[order[i]] > values[order[i + 1]]) {
                        done = false;
                        temp = order[i];
                        order[i] = order[i + 1];
                        order[i + 1] = temp;
                    }
                }
            }
        }
    }
