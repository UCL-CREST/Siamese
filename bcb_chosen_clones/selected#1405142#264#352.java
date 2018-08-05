    public KernelMatrix(String in, int n, boolean b) {
        super(n, n);
        if (n == 0) {
            System.err.println("Empty Matrix");
            System.exit(1);
        }
        BufferedReader labelreader = null;
        BufferedReader matrixreader = null;
        labels = new Labels();
        try {
            labelreader = new BufferedReader(new FileReader(in));
            matrixreader = new BufferedReader(new FileReader(in));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Vector<Double> t = new Vector<Double>();
        String line = "";
        int z = 0;
        try {
            while ((line = labelreader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                Double d = new Double(st.nextToken());
                if (!t.contains(d)) z++;
                t.add(d);
                labels.addIfNotContained(d);
            }
        } catch (NumberFormatException e2) {
            e2.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        values = new double[t.size()];
        double[][] series = new double[values.length][values.length];
        int[] ind = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = t.get(i).doubleValue();
            ind[i] = i;
        }
        t.clear();
        int i = 0;
        try {
            while ((line = matrixreader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                st.nextElement();
                st.nextElement();
                int j = 0;
                while (j < n) {
                    float sim = 0.0f;
                    String s = "";
                    try {
                        s = st.nextToken();
                    } catch (RuntimeException e1) {
                        s = "" + (j + 1) + ":0.0";
                    }
                    int g = 0;
                    int h = 0;
                    try {
                        g = s.indexOf(":");
                        h = Integer.parseInt(s.substring(0, g));
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        System.out.println(s);
                    }
                    sim = Float.parseFloat(s.substring(g + 1));
                    while (j < h - 1) {
                        series[i][j] = 0.0f;
                        j++;
                    }
                    series[i][j] = sim;
                    j++;
                }
                i++;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(values.length + " entries loaded");
        double[][] M = new double[n][n];
        for (i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                M[i][j] = RBF.rbf(series[i], series[j]);
                M[j][i] = M[i][j];
            }
        }
        super.setMatrix(ind, ind, new Matrix(M));
        class_ind = labels.getClassIndices();
    }
