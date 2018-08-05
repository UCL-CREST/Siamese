        public static void transpose(double[][] in, double[][] out) {
            if (D4.isIdentity(in)) {
                if (in != out) D4.setIdentity(out);
                return;
            }
            double[][] matIn = D4.getMatrixPool().borrowObject();
            try {
                double[][] copyIn;
                if (in == out) {
                    D4.copyMatrix(in, matIn);
                    copyIn = matIn;
                } else {
                    copyIn = in;
                }
                for (int rowI = 0; rowI < 4; rowI++) {
                    for (int colI = 0; colI < 4; colI++) {
                        out[colI][rowI] = copyIn[rowI][colI];
                    }
                }
            } finally {
                D4.getMatrixPool().returnObject(matIn);
            }
        }
