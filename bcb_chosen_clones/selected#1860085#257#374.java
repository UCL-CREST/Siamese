    public static void main(String[] argv) {
        PrintStream out = System.out;
        try {
            if (argv.length == 0) {
                System.out.println(" Syntax: java HCL infile.dat ");
                System.out.println(" Input file format: ");
                System.out.println(" Line 1: integer no. rows, no. cols.");
                System.out.println(" Successive lines: matrix values, floating");
                System.out.println(" Read in row-wise");
                System.exit(1);
            }
            String filname = argv[0];
            System.out.println(" Input file name: " + filname);
            FileInputStream is = new FileInputStream(filname);
            BufferedReader bis = new BufferedReader(new InputStreamReader(is));
            StreamTokenizer st = new StreamTokenizer(bis);
            st.nextToken();
            int nrow = (int) st.nval;
            st.nextToken();
            int ncol = (int) st.nval;
            System.out.println(" No. of rows, nrow = " + nrow);
            System.out.println(" No. of cols, ncol = " + ncol);
            double[][] indat = new double[nrow][ncol];
            double inval;
            System.out.println(" Input data sample follows as a check, first 4 values.");
            for (int i = 0; i < nrow; i++) {
                for (int j = 0; j < ncol; j++) {
                    st.nextToken();
                    inval = (double) st.nval;
                    indat[i][j] = inval;
                    if (i < 2 && j < 2) {
                        System.out.println(" value = " + inval);
                    }
                }
            }
            System.out.println();
            int[][] clusters = new int[nrow][nrow];
            int[] nn = new int[nrow];
            int[] flag = new int[nrow];
            double[] nndiss = new double[nrow];
            double[] clcard = new double[nrow];
            double[] mass = new double[nrow];
            double[] cpoids = new double[ncol];
            int minobs;
            double mindist;
            int ncl;
            ncl = nrow;
            for (int i = 0; i < nrow; i++) {
                flag[i] = 1;
                clcard[i] = 1.0;
                mass[i] = 1.0;
            }
            for (int j = 0; j < ncol; j++) {
                cpoids[j] = 0.0;
            }
            double[][] diss = new double[nrow][nrow];
            diss = dissim(nrow, ncol, mass, indat);
            System.out.println("Dissimilarity matrix for analysis:");
            printMatrix(nrow, nrow, diss, 4, 10);
            getNNs(nrow, flag, diss, nn, nndiss);
            int clust1 = 0;
            int clust2 = 0;
            int cl1 = 0;
            int cl2 = 0;
            clustMat(nrow, clusters, clust1, clust2, ncl);
            do {
                minobs = -1;
                mindist = MAXVAL;
                for (int i = 0; i < nrow; i++) {
                    if (flag[i] == 1) {
                        if (nndiss[i] < mindist) {
                            mindist = nndiss[i];
                            minobs = i;
                        }
                    }
                }
                if (minobs < nn[minobs]) {
                    clust1 = minobs + 1;
                    clust2 = nn[minobs];
                }
                if (minobs > nn[minobs]) {
                    clust2 = minobs + 1;
                    clust1 = nn[minobs];
                }
                System.out.println(" clus#1: " + clust1 + ";  clus#2: " + clust2 + ";  new card: " + (clcard[clust1 - 1] + clcard[clust2 - 1]) + "; # clus left: " + ncl + "; mindiss: " + mindist);
                ncl = ncl - 1;
                clustMat(nrow, clusters, clust1, clust2, ncl);
                cl1 = clust1 - 1;
                cl2 = clust2 - 1;
                for (int i = 0; i < nrow; i++) {
                    if ((i != cl1) && (i != cl2) && (flag[i] == 1)) {
                        diss[cl1][i] = (mass[cl1] + mass[i]) / (mass[cl1] + mass[cl2] + mass[i]) * diss[cl1][i] + (mass[cl2] + mass[i]) / (mass[cl1] + mass[cl2] + mass[i]) * diss[cl2][i] - (mass[i]) / (mass[cl1] + mass[cl2] + mass[i]) * diss[cl1][cl2];
                        diss[i][cl1] = diss[cl1][i];
                    }
                }
                clcard[cl1] = clcard[cl1] + clcard[cl2];
                mass[cl1] = mass[cl1] + mass[cl2];
                for (int i = 0; i < nrow; i++) {
                    diss[cl2][i] = MAXVAL;
                    diss[i][cl2] = diss[cl2][i];
                    flag[cl2] = 0;
                    nndiss[cl2] = MAXVAL;
                    mass[cl2] = 0;
                }
                getNNs(nrow, flag, diss, nn, nndiss);
            } while (ncl > 1);
            int[][] tclusters = new int[nrow][nrow];
            for (int i1 = 0; i1 < nrow; i1++) {
                for (int i2 = 0; i2 < nrow; i2++) {
                    tclusters[i2][i1] = clusters[i1][i2];
                }
            }
            printMatrix(nrow, nrow, tclusters, 4, 4);
        } catch (IOException e) {
            out.println("error: " + e);
            System.exit(1);
        }
    }
