    void shuffle(MersenneTwisterFast random) {
        int numObjs = fitnesses.length;
        float[] fitnesses = this.fitnesses;
        int[] indices = this.indices;
        float f;
        int i;
        int rand;
        for (int x = numObjs - 1; x >= 1; x--) {
            rand = random.nextInt(x + 1);
            f = fitnesses[x];
            fitnesses[x] = fitnesses[rand];
            fitnesses[rand] = f;
            i = indices[x];
            indices[x] = indices[rand];
            indices[rand] = i;
        }
    }
