    private short[] geraConfInicial(int n, short[] array) {
        int swap;
        short aux;
        Random random = new Random();
        for (int pos = n - 1; pos > 0; --pos) {
            swap = random.nextInt(pos + 1);
            aux = array[pos];
            array[pos] = array[swap];
            array[swap] = aux;
        }
        return array;
    }
