    public static int[] sortAscending(double input[]) {
        int[] order = new int[input.length];
        for (int i = 0; i < order.length; i++) order[i] = i;
        for (int i = input.length; --i >= 0; ) {
            for (int j = 0; j < i; j++) {
                if (input[j] > input[j + 1]) {
                    double mem = input[j];
                    input[j] = input[j + 1];
                    input[j + 1] = mem;
                    int id = order[j];
                    order[j] = order[j + 1];
                    order[j + 1] = id;
                }
            }
        }
        return order;
    }
