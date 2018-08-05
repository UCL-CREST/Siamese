    public static void svm_cross_validation(svm_problem prob, svm_parameter param, int nr_fold, double[] target) {
        int i;
        int[] fold_start = new int[nr_fold + 1];
        int l = prob.l;
        int[] perm = new int[l];
        if ((param.svm_type == svm_parameter.C_SVC || param.svm_type == svm_parameter.NU_SVC) && nr_fold < l) {
            int[] tmp_nr_class = new int[1];
            int[][] tmp_label = new int[1][];
            int[][] tmp_start = new int[1][];
            int[][] tmp_count = new int[1][];
            svm_group_classes(prob, tmp_nr_class, tmp_label, tmp_start, tmp_count, perm);
            int nr_class = tmp_nr_class[0];
            int[] start = tmp_start[0];
            int[] count = tmp_count[0];
            int[] fold_count = new int[nr_fold];
            int c;
            int[] index = new int[l];
            for (i = 0; i < l; i++) index[i] = perm[i];
            for (c = 0; c < nr_class; c++) for (i = 0; i < count[c]; i++) {
                int j = i + rand.nextInt(count[c] - i);
                do {
                    int _ = index[start[c] + j];
                    index[start[c] + j] = index[start[c] + i];
                    index[start[c] + i] = _;
                } while (false);
            }
            for (i = 0; i < nr_fold; i++) {
                fold_count[i] = 0;
                for (c = 0; c < nr_class; c++) fold_count[i] += (i + 1) * count[c] / nr_fold - i * count[c] / nr_fold;
            }
            fold_start[0] = 0;
            for (i = 1; i <= nr_fold; i++) fold_start[i] = fold_start[i - 1] + fold_count[i - 1];
            for (c = 0; c < nr_class; c++) for (i = 0; i < nr_fold; i++) {
                int begin = start[c] + i * count[c] / nr_fold;
                int end = start[c] + (i + 1) * count[c] / nr_fold;
                for (int j = begin; j < end; j++) {
                    perm[fold_start[i]] = index[j];
                    fold_start[i]++;
                }
            }
            fold_start[0] = 0;
            for (i = 1; i <= nr_fold; i++) fold_start[i] = fold_start[i - 1] + fold_count[i - 1];
        } else {
            for (i = 0; i < l; i++) perm[i] = i;
            for (i = 0; i < l; i++) {
                int j = i + rand.nextInt(l - i);
                do {
                    int _ = perm[i];
                    perm[i] = perm[j];
                    perm[j] = _;
                } while (false);
            }
            for (i = 0; i <= nr_fold; i++) fold_start[i] = i * l / nr_fold;
        }
        for (i = 0; i < nr_fold; i++) {
            int begin = fold_start[i];
            int end = fold_start[i + 1];
            int j, k;
            svm_problem subprob = new svm_problem();
            subprob.l = l - (end - begin);
            subprob.x = new svm_node[subprob.l][];
            subprob.y = new double[subprob.l];
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
            svm_model submodel = svm_train(subprob, param);
            if (param.probability == 1 && (param.svm_type == svm_parameter.C_SVC || param.svm_type == svm_parameter.NU_SVC)) {
                double[] prob_estimates = new double[svm_get_nr_class(submodel)];
                for (j = begin; j < end; j++) target[perm[j]] = svm_predict_probability(submodel, prob.x[perm[j]], prob_estimates);
            } else for (j = begin; j < end; j++) target[perm[j]] = svm_predict(submodel, prob.x[perm[j]]);
        }
    }
