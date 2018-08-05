        public static void transpose3(double[][] in, double[][] out) {
            if (D.isIdentityMatrix(in)) {
                if (in != out) D.identityMatrix(out);
                return;
            }
            double[][] copyIn;
            if (in == out) {
                D.copyMatrix(in, _dummyMatrix);
                copyIn = _dummyMatrix;
            } else {
                copyIn = in;
            }
            for (int rowI = 0; rowI < 3; rowI++) {
                for (int colI = 0; colI < 3; colI++) {
                    out[colI][rowI] = copyIn[rowI][colI];
                }
            }
        }
