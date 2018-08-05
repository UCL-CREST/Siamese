    public boolean batchFinished() throws Exception {
        Instances data = getInputFormat();
        if (data == null) throw new IllegalStateException("No input instance format defined");
        if (m_Converter == null) {
            int[] randomIndices = new int[m_ClassCounts.length];
            for (int i = 0; i < randomIndices.length; i++) {
                randomIndices[i] = i;
            }
            for (int j = randomIndices.length - 1; j > 0; j--) {
                int toSwap = m_Random.nextInt(j + 1);
                int tmpIndex = randomIndices[j];
                randomIndices[j] = randomIndices[toSwap];
                randomIndices[toSwap] = tmpIndex;
            }
            double[] randomizedCounts = new double[m_ClassCounts.length];
            for (int i = 0; i < randomizedCounts.length; i++) {
                randomizedCounts[i] = m_ClassCounts[randomIndices[i]];
            }
            if (m_ClassOrder == RANDOM) {
                m_Converter = randomIndices;
                m_ClassCounts = randomizedCounts;
            } else {
                int[] sorted = Utils.sort(randomizedCounts);
                m_Converter = new int[sorted.length];
                if (m_ClassOrder == FREQ_ASCEND) {
                    for (int i = 0; i < sorted.length; i++) {
                        m_Converter[i] = randomIndices[sorted[i]];
                    }
                } else if (m_ClassOrder == FREQ_DESCEND) {
                    for (int i = 0; i < sorted.length; i++) {
                        m_Converter[i] = randomIndices[sorted[sorted.length - i - 1]];
                    }
                } else {
                    throw new IllegalArgumentException("Class order not defined!");
                }
                double[] tmp2 = new double[m_ClassCounts.length];
                for (int i = 0; i < m_Converter.length; i++) {
                    tmp2[i] = m_ClassCounts[m_Converter[i]];
                }
                m_ClassCounts = tmp2;
            }
            FastVector values = new FastVector(data.classAttribute().numValues());
            for (int i = 0; i < data.numClasses(); i++) {
                values.addElement(data.classAttribute().value(m_Converter[i]));
            }
            FastVector newVec = new FastVector(data.numAttributes());
            for (int i = 0; i < data.numAttributes(); i++) {
                if (i == data.classIndex()) {
                    newVec.addElement(new Attribute(data.classAttribute().name(), values, data.classAttribute().getMetadata()));
                } else {
                    newVec.addElement(data.attribute(i));
                }
            }
            Instances newInsts = new Instances(data.relationName(), newVec, 0);
            newInsts.setClassIndex(data.classIndex());
            setOutputFormat(newInsts);
            int[] temp = new int[m_Converter.length];
            for (int i = 0; i < temp.length; i++) {
                temp[m_Converter[i]] = i;
            }
            m_Converter = temp;
            for (int xyz = 0; xyz < data.numInstances(); xyz++) {
                Instance datum = data.instance(xyz);
                if (!datum.isMissing(datum.classIndex())) {
                    datum.setClassValue((float) m_Converter[(int) datum.classValue()]);
                }
                push(datum);
            }
        }
        flushInput();
        m_NewBatch = true;
        return (numPendingOutput() != 0);
    }
