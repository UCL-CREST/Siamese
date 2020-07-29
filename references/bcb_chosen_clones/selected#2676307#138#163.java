        static double[][] evalBurdenMatrix(AtomContainer ac, double[] vsd) {
            AtomContainer local = AtomContainerManipulator.removeHydrogens(ac);
            int natom = local.getAtomCount();
            double[][] m = new double[natom][natom];
            for (int i = 0; i < natom - 1; i++) {
                for (int j = i + 1; j < natom; j++) {
                    for (int k = 0; k < local.getBondCount(); k++) {
                        Bond b = local.getBondAt(k);
                        if (b.contains(local.getAtomAt(i)) && b.contains(local.getAtomAt(j))) {
                            if (b.getOrder() == CDKConstants.BONDORDER_SINGLE) m[i][j] = 0.1; else if (b.getOrder() == CDKConstants.BONDORDER_DOUBLE) m[i][j] = 0.2; else if (b.getOrder() == CDKConstants.BONDORDER_TRIPLE) m[i][j] = 0.3; else if (b.getOrder() == CDKConstants.BONDORDER_AROMATIC) m[i][j] = 0.15;
                            if (local.getBondCount(i) == 1 || local.getBondCount(j) == 1) {
                                m[i][j] += 0.01;
                            }
                            m[j][i] = m[i][j];
                        } else {
                            m[i][j] = 0.001;
                            m[j][i] = 0.001;
                        }
                    }
                }
            }
            for (int i = 0; i < natom; i++) {
                if (vsd != null) m[i][i] = vsd[i]; else m[i][i] = 0.0;
            }
            return (m);
        }
