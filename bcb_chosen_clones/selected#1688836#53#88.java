    public static void crossValidation(Problem prob, Parameter param, int nr_fold, int[] target) {
        int i;
        int[] fold_start = new int[nr_fold + 1];
        int l = prob.l;
        int[] perm = new int[l];
        for (i = 0; i < l; i++) perm[i] = i;
        for (i = 0; i < l; i++) {
            int j = i + random.nextInt(l - i);
            swap(perm, i, j);
        }
        for (i = 0; i <= nr_fold; i++) fold_start[i] = i * l / nr_fold;
        for (i = 0; i < nr_fold; i++) {
            int begin = fold_start[i];
            int end = fold_start[i + 1];
            int j, k;
            Problem subprob = new Problem();
            subprob.bias = prob.bias;
            subprob.n = prob.n;
            subprob.l = l - (end - begin);
            subprob.x = new FeatureNode[subprob.l][];
            subprob.y = new int[subprob.l];
            k = 0;
            for (j = 0; j < begin; j++) {
                subprob.x[k] = prob.x[perm[j]];
                subprob.y[k] = prob.y[perm[j]];
                ++k;
            }
            for (j = end; j < l; j++) {
                subprob.x[k] = prob.x[perm[j]];
                subprob.y[k] = prob.y[perm[j]];
                ++k;
            }
            Model submodel = train(subprob, param);
            for (j = begin; j < end; j++) target[perm[j]] = predict(submodel, prob.x[perm[j]]);
        }
    }
