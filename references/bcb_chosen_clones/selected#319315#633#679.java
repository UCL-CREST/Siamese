    public String[][] getLHSJobList(int LHSsize) {
        String[][] JobList = new String[LHSsize][];
        if (ParamTree != null) {
            DefaultMutableTreeNode thisleaf = ParamTree.getFirstLeaf();
            Object[] path = thisleaf.getUserObjectPath();
            int length = path.length + 3;
            String[][] SampledValues = new String[length][];
            int n_alt = 1;
            n_alt = this.getFileList(this.resolveWeatherDir(), this.getWeatherFile()).size();
            int[] SampledIndex = this.defaultLHSdiscreteSample(LHSsize, n_alt);
            SampledValues[1] = new String[LHSsize];
            for (int j = 0; j < LHSsize; j++) {
                SampledValues[1][j] = Integer.toString(SampledIndex[j]);
            }
            n_alt = this.getFileList(this.resolveIDFDir(), this.getIDFTemplate()).size();
            SampledIndex = this.defaultLHSdiscreteSample(LHSsize, n_alt);
            SampledValues[2] = new String[LHSsize];
            for (int j = 0; j < LHSsize; j++) {
                SampledValues[2][j] = Integer.toString(SampledIndex[j]);
            }
            for (int i = 3; i < length; i++) {
                ParameterItem Param = ((ParameterItem) path[i - 3]);
                if (Param.getValuesString().startsWith("@sample")) {
                    SampledValues[i] = this.defaultLHSdistributionSample(LHSsize, Param.getValuesString(), Param.getType());
                } else {
                    n_alt = Param.getNAltValues();
                    SampledIndex = this.defaultLHSdiscreteSample(LHSsize, n_alt);
                    SampledValues[i] = new String[LHSsize];
                    for (int j = 0; j < LHSsize; j++) {
                        SampledValues[i][j] = Param.getAlternativeValues()[SampledIndex[j]];
                    }
                }
            }
            for (int i = 1; i < length; i++) {
                Collections.shuffle(Arrays.asList(SampledValues[i]), RandomSource.getRandomGenerator());
            }
            for (int i = 0; i < LHSsize; i++) {
                JobList[i] = new String[length];
                JobList[i][0] = new Formatter().format("LHS-%06d", i).toString();
                for (int j = 1; j < length; j++) {
                    JobList[i][j] = SampledValues[j][i];
                }
            }
            return JobList;
        }
        return null;
    }
