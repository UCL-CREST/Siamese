        public static void transpose(double[][] in, double[][] out) {
            if (D3.isIdentity(in)) {
                if (in != out) D3.setIdentity(out);
                return;
            }
            double[][] matIn = D3.getMatrixPool().borrowObject();
            try {
                double[][] copyIn;
                if (in == out) {
                    D3.copyMatrix(in, matIn);
                    copyIn = matIn;
                } else {
                    copyIn = in;
                }
                for (int rowI = 0; rowI < 3; rowI++) {
                    for (int colI = 0; colI < 3; colI++) {
                        out[colI][rowI] = copyIn[rowI][colI];
                    }
                }
            } finally {
                D3.getMatrixPool().returnObject(matIn);
            }
        }
