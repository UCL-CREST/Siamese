    @Override
    public KSA00 getEstimate() {
        int probe_count = getData().getModel().getProbes().size();
        int ch_length = getData().getModel().getChromosomeLength();
        int clone_length = getData().getModel().getCloneLength();
        int clone_count = getData().cloneCount();
        int counter = 0;
        byte[][] scores = new byte[clone_count][];
        for (KSA00Data.Hybridization val : getData()) {
            scores[counter++] = val.getScore();
        }
        int[] probeOrder = ArrayUtil.createSequence(getData().getModel().getProbes().size(), false);
        int[][] joint = new int[probe_count][probe_count];
        for (int probeIndex1 = 0; probeIndex1 < probe_count; probeIndex1++) {
            for (int probeIndex2 = probeIndex1 + 1; probeIndex2 < probe_count; probeIndex2++) {
                for (int cloneIndex = 0; cloneIndex < clone_count; cloneIndex++) {
                    if ((scores[cloneIndex][probeIndex1] == 1) && (scores[cloneIndex][probeIndex2] == 1)) {
                        joint[probeIndex1][probeIndex2]++;
                    }
                }
                joint[probeIndex2][probeIndex1] = joint[probeIndex1][probeIndex2];
            }
        }
        int gap = ch_length - (probe_count * clone_length);
        double[] y = Initial_Ys(probeOrder, probe_count, clone_count, clone_length, gap, joint);
        return getData().getModel().create(Arrays.asList(ArrayUtils.toObject(y)));
    }
