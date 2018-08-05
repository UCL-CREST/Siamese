    public KernelMatrix(Source source, Kernel kernel) {
        super(source.getSize(), source.getSize());
        double[][] matrix = new double[source.getSize()][source.getSize()];
        values = new double[source.getSize()];
        KernelMatrixTimer kmt = new KernelMatrixTimer(source.getSize(), Thread.currentThread(), 30);
        kmt.start();
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j <= i; j++) {
                Object o_i = source.getInstance(i);
                Object o_j = source.getInstance(j);
                matrix[i][j] = kernel.kernel(o_i, o_j);
                matrix[j][i] = matrix[i][j];
                kmt.calculationdone();
                values[i] = source.getValue(i);
            }
        }
        kmt.finalreport();
        kmt.stop();
        labels = new Labels();
        int[] ind = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            ind[i] = i;
            labels.addIfNotContained(values[i]);
        }
        super.setMatrix(ind, ind, new Matrix(matrix));
        class_ind = labels.getClassIndices();
        System.out.println("Matrix for " + kernel.getIdentifier() + " successfully generated");
    }
