    public static void main(String args[]) throws FileNotFoundException {
        LDACmdOption ldaOption = new LDACmdOption();
        ldaOption.inf = true;
        ldaOption.dir = "D:/programs/eclipse/workspace/testproject/src/model/jgibblda/models/";
        ldaOption.modelName = "model-final";
        ldaOption.niters = 100;
        Inferencer inferencer = new Inferencer();
        inferencer.init(ldaOption);
        ldaOption.dfile = "newdocs.dat";
        Model newModel = inferencer.inference();
        double[][] sim = new double[newModel.M][newModel.M];
        double s1;
        double s2;
        double s3;
        for (int m = 0; m < newModel.M; m++) {
            for (int j = m + 1; j < newModel.M; j++) {
                s1 = 0;
                s2 = 0;
                s3 = 0;
                for (int k = 0; k < newModel.K; k++) {
                    s1 += newModel.theta[m][k] * newModel.theta[j][k];
                    s2 += Math.sqrt(newModel.theta[m][k] * newModel.theta[m][k]);
                    s3 += Math.sqrt(newModel.theta[j][k] * newModel.theta[j][k]);
                }
                double a = s1 / (s2 * s3);
                sim[m][j] = a;
            }
        }
        for (int i = 0; i < newModel.K; i++) {
            sim[i][i] = 1;
            for (int j = 0; j < i - 1; j++) {
                sim[i][j] = sim[j][i];
            }
        }
        sim[1][0] = sim[0][1];
        PrintWriter q = new PrintWriter(new FileOutputStream("D:/programs/eclipse/workspace/testproject/src/model/jgibblda/models/cosinesim.out"), true);
        for (int i = 0; i < newModel.M; i++) {
            for (int j = 0; j < newModel.M; j++) {
                q.print(sim[i][j] + " ");
            }
            q.println("\n");
        }
        System.out.println("similarity calculation completed");
    }
