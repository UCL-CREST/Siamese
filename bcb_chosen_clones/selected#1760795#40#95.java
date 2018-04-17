    private void run() {
        setMessage("Physical Likleihood Computation has Started");
        setProgress(10);
        Chromosome chr = Chromosome.getInstance(probeMap.getLinkageGroup());
        int probe_count = probeMap.getProbes().size();
        int clone_count = chr.getClones().size();
        int ch_length = chr.getLength();
        byte[][] data = chr.getHybridizationData(probeMap.getProbes());
        double probe_false_pos = chr.getProbFalsePos();
        double probe_false_neg = chr.getProbFalseNeg();
        int clone_length = GWData.getInstance().getCloneLength();
        int[] probeOrder = ArrayUtils.toPrimitive(chr.getProbeOrder(probeMap.getProbes()).toArray(new Integer[0]));
        int[][] joint = new int[probe_count][probe_count];
        for (int probeIndex1 = 0; probeIndex1 < probe_count; probeIndex1++) {
            for (int probeIndex2 = probeIndex1 + 1; probeIndex2 < probe_count; probeIndex2++) {
                for (int cloneIndex = 0; cloneIndex < clone_count; cloneIndex++) {
                    if ((data[cloneIndex][probeIndex1] == 1) && (data[cloneIndex][probeIndex2] == 1)) {
                        joint[probeIndex1][probeIndex2]++;
                    }
                }
                joint[probeIndex2][probeIndex1] = joint[probeIndex1][probeIndex2];
            }
        }
        double[][] aa = new double[clone_count][probe_count + 1];
        for (int cloneIndex = 0; cloneIndex < clone_count; cloneIndex++) {
            for (int probeIndex = 0; probeIndex < probe_count + 1; probeIndex++) {
                if (probeIndex == 0) {
                    aa[cloneIndex][probeIndex] = 0.0;
                } else if (data[cloneIndex][probeIndex - 1] == 0) {
                    aa[cloneIndex][probeIndex] = probe_false_neg / (1 - probe_false_pos);
                } else {
                    aa[cloneIndex][probeIndex] = (1 - probe_false_neg) / probe_false_pos;
                }
            }
        }
        int P = 0;
        for (int cloneIndex = 0; cloneIndex < clone_count; cloneIndex++) {
            for (int probeIndex = 0; probeIndex < probe_count; probeIndex++) {
                if (data[cloneIndex][probeIndex] == 1) {
                    P++;
                }
            }
        }
        double const1 = clone_count * Math.log(ch_length - clone_length) - P * Math.log(probe_false_pos / (1 - probe_false_pos)) - probe_count * clone_count * Math.log(1 - probe_false_pos);
        int gap = ch_length - (probe_count * clone_length);
        setMessage("Computing initial probe spacings");
        setProgress(30);
        double[] y = PLHoodHelper.Initial_Ys(probeOrder, probe_count, clone_count, ch_length, clone_length, gap, joint);
        double[] R = PLHoodHelper.getR(ch_length, clone_length, probe_count, clone_count, aa, probeOrder);
        setMessage("Running Steepest Descent Algorithm");
        setProgress(70);
        logLikelihood = -PLHoodHelper.Conj(y, probe_count, clone_count, aa, probeOrder, clone_length, const1, R);
        probeSpacing = Arrays.asList(ArrayUtils.toObject(y));
        setMessage("The Physical Log-likelihood is " + logLikelihood);
        setProgress(100);
    }
