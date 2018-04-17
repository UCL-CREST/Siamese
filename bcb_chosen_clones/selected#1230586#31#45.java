    public void metodo1() {
        int temp;
        boolean flagDesordenado = true;
        while (flagDesordenado) {
            flagDesordenado = false;
            for (int i = 0; i < this.tamanoTabla - 1; i++) {
                if (tabla[i] > tabla[i + 1]) {
                    flagDesordenado = true;
                    temp = tabla[i];
                    tabla[i] = tabla[i + 1];
                    tabla[i + 1] = temp;
                }
            }
        }
    }
