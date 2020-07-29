        static double[][] evalMatrix(IAtomContainer atomContainer, double[] vsd) {
            IAtomContainer local = AtomContainerManipulator.removeHydrogens(atomContainer);
            int natom = local.getAtomCount();
            double[][] matrix = new double[natom][natom];
            for (int i = 0; i < natom; i++) {
                for (int j = 0; j < natom; j++) {
                    matrix[i][j] = 0.0;
                }
            }
            for (int i = 0; i < natom - 1; i++) {
                for (int j = i + 1; j < natom; j++) {
                    for (int k = 0; k < local.getBondCount(); k++) {
                        IBond bond = local.getBond(k);
                        if (bond.contains(local.getAtom(i)) && bond.contains(local.getAtom(j))) {
                            if (bond.getFlag(CDKConstants.ISAROMATIC)) matrix[i][j] = 0.15; else if (bond.getOrder() == CDKConstants.BONDORDER_SINGLE) matrix[i][j] = 0.1; else if (bond.getOrder() == CDKConstants.BONDORDER_DOUBLE) matrix[i][j] = 0.2; else if (bond.getOrder() == CDKConstants.BONDORDER_TRIPLE) matrix[i][j] = 0.3;
                            if (local.getConnectedBondsCount(i) == 1 || local.getConnectedBondsCount(j) == 1) {
                                matrix[i][j] += 0.01;
                            }
                            matrix[j][i] = matrix[i][j];
                        } else {
                            matrix[i][j] = 0.001;
                            matrix[j][i] = 0.001;
                        }
                    }
                }
            }
            for (int i = 0; i < natom; i++) {
                if (vsd != null) matrix[i][i] = vsd[i]; else matrix[i][i] = 0.0;
            }
            return (matrix);
        }
