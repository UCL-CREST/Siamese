    void sortclasses() {
        int i, j;
        boolean domore;
        vclassptr = new int[numc];
        for (i = 0; i < numc; i++) vclassptr[i] = i;
        domore = true;
        while (domore == true) {
            domore = false;
            for (i = 0; i < numc - 1; i++) {
                if (vclassctr[vclassptr[i]] < vclassctr[vclassptr[i + 1]]) {
                    int temp = vclassptr[i];
                    vclassptr[i] = vclassptr[i + 1];
                    vclassptr[i + 1] = temp;
                    domore = true;
                }
            }
        }
    }
