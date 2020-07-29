    private LinkedList<Datum> processDatum(Datum dataset) {
        ArrayList<Object[]> markerTestResults = new ArrayList<Object[]>();
        ArrayList<Object[]> alleleEstimateResults = new ArrayList<Object[]>();
        boolean hasAlleleNames = false;
        String blank = new String("");
        MarkerPhenotypeAdapter theAdapter;
        if (dataset.getDataType().equals(MarkerPhenotype.class)) {
            theAdapter = new MarkerPhenotypeAdapter((MarkerPhenotype) dataset.getData());
        } else theAdapter = new MarkerPhenotypeAdapter((Phenotype) dataset.getData());
        int numberOfMarkers = theAdapter.getNumberOfMarkers();
        if (numberOfMarkers == 0) {
            return calculateBLUEsFromPhenotypes(theAdapter, dataset.getName());
        }
        int numberOfCovariates = theAdapter.getNumberOfCovariates();
        int numberOfFactors = theAdapter.getNumberOfFactors();
        int numberOfPhenotypes = theAdapter.getNumberOfPhenotypes();
        int expectedIterations = numberOfPhenotypes * numberOfMarkers;
        int iterationsSofar = 0;
        int percentFinished = 0;
        File tempFile = null;
        File ftestFile = null;
        File blueFile = null;
        BufferedWriter ftestWriter = null;
        BufferedWriter BLUEWriter = null;
        String ftestHeader = "Trait\tMarker\tLocus\tLocus_pos\tChr\tChr_pos\tmarker_F\tmarker_p\tperm_p\tmarkerR2\tmarkerDF\tmarkerMS\terrorDF\terrorMS\tmodelDF\tmodelMS";
        String BLUEHeader = "Trait\tMarker\tObs\tLocus\tLocus_pos\tChr\tChr_pos\tAllele\tEstimate";
        if (writeOutputToFile) {
            String outputbase = outputName;
            if (outputbase.endsWith(".txt")) {
                int index = outputbase.lastIndexOf(".");
                outputbase = outputbase.substring(0, index);
            }
            String datasetNameNoSpace = dataset.getName().trim().replaceAll("\\ ", "_");
            ftestFile = new File(outputbase + "_" + datasetNameNoSpace + "_ftest.txt");
            int count = 0;
            while (ftestFile.exists()) {
                count++;
                ftestFile = new File(outputbase + "_" + datasetNameNoSpace + "_ftest" + count + ".txt");
            }
            blueFile = new File(outputbase + "_" + datasetNameNoSpace + "_BLUEs.txt");
            count = 0;
            while (blueFile.exists()) {
                count++;
                blueFile = new File(outputbase + "_" + datasetNameNoSpace + "_BLUEs" + count + ".txt");
            }
            tempFile = new File(outputbase + "_" + datasetNameNoSpace + "_ftest.tmp");
            try {
                if (permute) {
                    ftestWriter = new BufferedWriter(new FileWriter(tempFile));
                    ftestWriter.write(ftestHeader);
                    ftestWriter.newLine();
                } else {
                    ftestWriter = new BufferedWriter(new FileWriter(ftestFile));
                    ftestWriter.write(ftestHeader);
                    ftestWriter.newLine();
                }
                if (reportBLUEs) {
                    BLUEWriter = new BufferedWriter(new FileWriter(blueFile));
                    BLUEWriter.write(BLUEHeader);
                    BLUEWriter.newLine();
                }
            } catch (IOException e) {
                myLogger.error("Failed to open file for output");
                myLogger.error(e);
                return null;
            }
        }
        if (permute) {
            minP = new double[numberOfPhenotypes][numberOfPermutations];
            for (int i = 0; i < numberOfPermutations; i++) {
                for (int j = 0; j < numberOfPhenotypes; j++) {
                    minP[j][i] = 1;
                }
            }
        }
        for (int ph = 0; ph < numberOfPhenotypes; ph++) {
            double[] phenotypeData = theAdapter.getPhenotypeValues(ph);
            boolean[] missing = theAdapter.getMissingPhenotypes(ph);
            ArrayList<String[]> factorList = MarkerPhenotypeAdapterUtils.getFactorList(theAdapter, ph, missing);
            ArrayList<double[]> covariateList = MarkerPhenotypeAdapterUtils.getCovariateList(theAdapter, ph, missing);
            double[][] permutedData = null;
            if (permute) {
                permutedData = permuteData(phenotypeData, missing, factorList, covariateList, theAdapter);
            }
            for (int m = 0; m < numberOfMarkers; m++) {
                Object[] markerData = theAdapter.getMarkerValue(ph, m);
                boolean[] finalMissing = new boolean[missing.length];
                System.arraycopy(missing, 0, finalMissing, 0, missing.length);
                MarkerPhenotypeAdapterUtils.updateMissing(finalMissing, theAdapter.getMissingMarkers(ph, m));
                int[] nonmissingRows = MarkerPhenotypeAdapterUtils.getNonMissingIndex(finalMissing);
                int numberOfObs = nonmissingRows.length;
                double[] y = new double[numberOfObs];
                for (int i = 0; i < numberOfObs; i++) y[i] = phenotypeData[nonmissingRows[i]];
                int firstMarkerAlleleEstimate = 1;
                ArrayList<ModelEffect> modelEffects = new ArrayList<ModelEffect>();
                FactorModelEffect meanEffect = new FactorModelEffect(new int[numberOfObs], false);
                meanEffect.setID("mean");
                modelEffects.add(meanEffect);
                if (numberOfFactors > 0) {
                    for (int f = 0; f < numberOfFactors; f++) {
                        String[] afactor = factorList.get(f);
                        String[] factorLabels = new String[numberOfObs];
                        for (int i = 0; i < numberOfObs; i++) factorLabels[i] = afactor[nonmissingRows[i]];
                        FactorModelEffect fme = new FactorModelEffect(ModelEffectUtils.getIntegerLevels(factorLabels), true, theAdapter.getFactorName(f));
                        modelEffects.add(fme);
                        firstMarkerAlleleEstimate += fme.getNumberOfLevels() - 1;
                    }
                }
                if (numberOfCovariates > 0) {
                    for (int c = 0; c < numberOfCovariates; c++) {
                        double[] covar = new double[numberOfObs];
                        double[] covariateData = covariateList.get(c);
                        for (int i = 0; i < numberOfObs; i++) covar[i] = covariateData[nonmissingRows[i]];
                        modelEffects.add(new CovariateModelEffect(covar, theAdapter.getCovariateName(c)));
                        firstMarkerAlleleEstimate++;
                    }
                }
                ModelEffect markerEffect;
                boolean markerIsDiscrete = theAdapter.isMarkerDiscrete(m);
                ArrayList<Object> alleleNames = new ArrayList<Object>();
                if (markerIsDiscrete) {
                    Object[] markers = new Object[numberOfObs];
                    for (int i = 0; i < numberOfObs; i++) markers[i] = markerData[nonmissingRows[i]];
                    int[] markerLevels = ModelEffectUtils.getIntegerLevels(markers, alleleNames);
                    markerEffect = new FactorModelEffect(markerLevels, true, theAdapter.getMarkerName(m));
                    hasAlleleNames = true;
                } else {
                    double[] markerdbl = new double[numberOfObs];
                    for (int i = 0; i < numberOfObs; i++) markerdbl[i] = ((Double) markerData[nonmissingRows[i]]).doubleValue();
                    markerEffect = new CovariateModelEffect(markerdbl, theAdapter.getMarkerName(m));
                }
                int[] alleleCounts = markerEffect.getLevelCounts();
                modelEffects.add(markerEffect);
                int markerEffectNumber = modelEffects.size() - 1;
                Identifier[] taxaSublist = new Identifier[numberOfObs];
                Identifier[] taxa = theAdapter.getTaxa(ph);
                for (int i = 0; i < numberOfObs; i++) taxaSublist[i] = taxa[nonmissingRows[i]];
                boolean areTaxaReplicated = containsDuplicates(taxaSublist);
                double[] markerSSdf = null, errorSSdf = null, modelSSdf = null;
                double F, p;
                double[] beta = null;
                if (areTaxaReplicated && markerIsDiscrete) {
                    ModelEffect taxaEffect = new FactorModelEffect(ModelEffectUtils.getIntegerLevels(taxaSublist), true);
                    modelEffects.add(taxaEffect);
                    SweepFastNestedModel sfnm = new SweepFastNestedModel(modelEffects, y);
                    double[] taxaSSdf = sfnm.getTaxaInMarkerSSdf();
                    double[] residualSSdf = sfnm.getErrorSSdf();
                    markerSSdf = sfnm.getMarkerSSdf();
                    errorSSdf = sfnm.getErrorSSdf();
                    modelSSdf = sfnm.getModelcfmSSdf();
                    F = markerSSdf[0] / markerSSdf[1] / taxaSSdf[0] * taxaSSdf[1];
                    try {
                        p = LinearModelUtils.Ftest(F, markerSSdf[1], taxaSSdf[1]);
                    } catch (Exception e) {
                        p = Double.NaN;
                    }
                    beta = sfnm.getBeta();
                    int markerdf = (int) markerSSdf[1];
                    if (permute && markerdf > 0) {
                        updatePermutationPValues(ph, permutedData, nonMissingIndex(missing, finalMissing), getXfromModelEffects(modelEffects), sfnm.getInverseOfXtX(), markerdf);
                    }
                } else {
                    SweepFastLinearModel sflm = new SweepFastLinearModel(modelEffects, y);
                    modelSSdf = sflm.getModelcfmSSdf();
                    markerSSdf = sflm.getMarginalSSdf(markerEffectNumber);
                    errorSSdf = sflm.getResidualSSdf();
                    F = markerSSdf[0] / markerSSdf[1] / errorSSdf[0] * errorSSdf[1];
                    try {
                        p = LinearModelUtils.Ftest(F, markerSSdf[1], errorSSdf[1]);
                    } catch (Exception e) {
                        p = Double.NaN;
                    }
                    beta = sflm.getBeta();
                    int markerdf = (int) markerSSdf[1];
                    if (permute && markerdf > 0) {
                        updatePermutationPValues(ph, permutedData, nonMissingIndex(missing, finalMissing), getXfromModelEffects(modelEffects), sflm.getInverseOfXtX(), markerdf);
                    }
                }
                if (!filterOutput || p < maxp) {
                    String traitname = theAdapter.getPhenotypeName(ph);
                    if (traitname == null) traitname = blank;
                    String marker = theAdapter.getMarkerName(m);
                    if (marker == null) marker = blank;
                    String locus = theAdapter.getLocusName(m);
                    Integer site = new Integer(theAdapter.getLocusPosition(m));
                    String chrname = "";
                    Double chrpos = Double.NaN;
                    if (hasMap) {
                        int ndx = -1;
                        ndx = myMap.getMarkerIndex(marker);
                        if (ndx > -1) {
                            chrname = myMap.getChromosome(ndx);
                            chrpos = myMap.getGeneticPosition(ndx);
                        }
                    }
                    Object[] result = new Object[16];
                    int col = 0;
                    result[col++] = traitname;
                    result[col++] = marker;
                    result[col++] = locus;
                    result[col++] = site;
                    result[col++] = chrname;
                    result[col++] = chrpos;
                    result[col++] = new Double(F);
                    result[col++] = new Double(p);
                    result[col++] = Double.NaN;
                    result[col++] = new Double(markerSSdf[0] / (modelSSdf[0] + errorSSdf[0]));
                    result[col++] = new Double(markerSSdf[1]);
                    result[col++] = new Double(markerSSdf[0] / markerSSdf[1]);
                    result[col++] = new Double(errorSSdf[1]);
                    result[col++] = new Double(errorSSdf[0] / errorSSdf[1]);
                    result[col++] = new Double(modelSSdf[1]);
                    result[col++] = new Double(modelSSdf[0] / modelSSdf[1]);
                    if (writeOutputToFile) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(result[0]);
                        for (int i = 1; i < 16; i++) sb.append("\t").append(result[i]);
                        try {
                            ftestWriter.write(sb.toString());
                            ftestWriter.newLine();
                        } catch (IOException e) {
                            myLogger.error("Failed to write output to ftest file. Ending prematurely");
                            try {
                                ftestWriter.flush();
                                BLUEWriter.flush();
                            } catch (Exception e1) {
                            }
                            myLogger.error(e);
                            return null;
                        }
                    } else {
                        markerTestResults.add(result);
                    }
                    int numberOfMarkerAlleles = alleleNames.size();
                    if (numberOfMarkerAlleles == 0) numberOfMarkerAlleles++;
                    for (int i = 0; i < numberOfMarkerAlleles; i++) {
                        result = new Object[9];
                        result[0] = traitname;
                        result[1] = marker;
                        result[2] = new Integer(alleleCounts[i]);
                        result[3] = locus;
                        result[4] = site;
                        result[5] = chrname;
                        result[6] = chrpos;
                        if (numberOfMarkerAlleles == 1) result[7] = ""; else result[7] = alleleNames.get(i);
                        if (i == numberOfMarkerAlleles - 1) result[8] = 0.0; else result[8] = beta[firstMarkerAlleleEstimate + i];
                        if (writeOutputToFile) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(result[0]);
                            for (int j = 1; j < 9; j++) sb.append("\t").append(result[j]);
                            try {
                                BLUEWriter.write(sb.toString());
                                BLUEWriter.newLine();
                            } catch (IOException e) {
                                myLogger.error("Failed to write output to ftest file. Ending prematurely");
                                try {
                                    ftestWriter.flush();
                                    BLUEWriter.flush();
                                } catch (Exception e1) {
                                }
                                myLogger.error(e);
                                return null;
                            }
                        } else {
                            alleleEstimateResults.add(result);
                        }
                    }
                }
                int tmpPercent = ++iterationsSofar * 100 / expectedIterations;
                if (tmpPercent > percentFinished) {
                    percentFinished = tmpPercent;
                    fireProgress(percentFinished);
                }
            }
        }
        fireProgress(0);
        if (writeOutputToFile) {
            try {
                ftestWriter.close();
                BLUEWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        HashMap<String, Integer> traitnameMap = new HashMap<String, Integer>();
        if (permute) {
            for (int ph = 0; ph < numberOfPhenotypes; ph++) {
                Arrays.sort(minP[ph]);
                traitnameMap.put(theAdapter.getPhenotypeName(ph), ph);
            }
            if (writeOutputToFile) {
                try {
                    BufferedReader tempReader = new BufferedReader(new FileReader(tempFile));
                    ftestWriter = new BufferedWriter(new FileWriter(ftestFile));
                    ftestWriter.write(tempReader.readLine());
                    ftestWriter.newLine();
                    String input;
                    String[] data;
                    Pattern tab = Pattern.compile("\t");
                    while ((input = tempReader.readLine()) != null) {
                        data = tab.split(input);
                        String trait = data[0];
                        double pval = Double.parseDouble(data[7]);
                        int ph = traitnameMap.get(trait);
                        int ndx = Arrays.binarySearch(minP[ph], pval);
                        if (ndx < 0) ndx = -ndx - 1;
                        if (ndx == 0) ndx = 1;
                        data[8] = Double.toString((double) ndx / (double) numberOfPermutations);
                        ftestWriter.write(data[0]);
                        for (int i = 1; i < data.length; i++) {
                            ftestWriter.write("\t");
                            ftestWriter.write(data[i]);
                        }
                        ftestWriter.newLine();
                    }
                    tempReader.close();
                    ftestWriter.close();
                    tempFile.delete();
                } catch (IOException e) {
                    myLogger.error(e);
                }
            } else {
                for (Object[] result : markerTestResults) {
                    String trait = result[0].toString();
                    double pval = (Double) result[7];
                    int ph = traitnameMap.get(trait);
                    int ndx = Arrays.binarySearch(minP[ph], pval);
                    if (ndx < 0) ndx = -ndx - 1;
                    if (ndx == 0) ndx = 1;
                    result[8] = new Double((double) ndx / (double) numberOfPermutations);
                }
            }
        }
        String[] columnLabels = new String[] { "Trait", "Marker", "Locus", "Locus_pos", "Chr", "Chr_pos", "marker_F", "marker_p", "perm_p", "markerR2", "markerDF", "markerMS", "errorDF", "errorMS", "modelDF", "modelMS" };
        boolean hasMarkerNames = theAdapter.hasMarkerNames();
        LinkedList<Integer> outputList = new LinkedList<Integer>();
        outputList.add(0);
        if (hasMarkerNames) outputList.add(1);
        outputList.add(2);
        outputList.add(3);
        if (hasMap) {
            outputList.add(4);
            outputList.add(5);
        }
        outputList.add(6);
        outputList.add(7);
        if (permute) outputList.add(8);
        for (int i = 9; i < 16; i++) outputList.add(i);
        LinkedList<Datum> resultset = new LinkedList<Datum>();
        int nrows = markerTestResults.size();
        Object[][] table = new Object[nrows][];
        int numberOfColumns = outputList.size();
        String[] colnames = new String[numberOfColumns];
        int count = 0;
        for (Integer ndx : outputList) colnames[count++] = columnLabels[ndx];
        for (int i = 0; i < nrows; i++) {
            table[i] = new Object[numberOfColumns];
            Object[] result = markerTestResults.get(i);
            count = 0;
            for (Integer ndx : outputList) table[i][count++] = result[ndx];
        }
        StringBuilder tableName = new StringBuilder("GLM_marker_test_");
        tableName.append(dataset.getName());
        StringBuilder comments = new StringBuilder("Tests of Marker-Phenotype Association");
        comments.append("GLM: fixed effect linear model\n");
        comments.append("Data set: ").append(dataset.getName());
        comments.append("\nmodel: trait = mean");
        for (int i = 0; i < theAdapter.getNumberOfFactors(); i++) {
            comments.append(" + ");
            comments.append(theAdapter.getFactorName(i));
        }
        for (int i = 0; i < theAdapter.getNumberOfCovariates(); i++) {
            comments.append(" + ");
            comments.append(theAdapter.getCovariateName(i));
        }
        comments.append(" + marker");
        if (writeOutputToFile) {
            comments.append("\nOutput written to " + ftestFile.getPath());
        }
        TableReport markerTestReport = new SimpleTableReport("Marker Test", colnames, table);
        resultset.add(new Datum(tableName.toString(), markerTestReport, comments.toString()));
        int[] outputIndex;
        columnLabels = new String[] { "Trait", "Marker", "Obs", "Locus", "Locus_pos", "Chr", "Chr_pos", "Allele", "Estimate" };
        if (hasAlleleNames) {
            if (hasMarkerNames && hasMap) {
                outputIndex = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
            } else if (hasMarkerNames) {
                outputIndex = new int[] { 0, 1, 2, 3, 4, 7, 8 };
            } else if (hasMap) {
                outputIndex = new int[] { 0, 2, 3, 4, 5, 6, 7, 8 };
            } else {
                outputIndex = new int[] { 0, 2, 3, 4, 7, 8 };
            }
        } else {
            if (hasMarkerNames && hasMap) {
                outputIndex = new int[] { 0, 1, 2, 3, 4, 5, 6, 8 };
            } else if (hasMarkerNames) {
                outputIndex = new int[] { 0, 1, 2, 3, 4, 8 };
            } else if (hasMap) {
                outputIndex = new int[] { 0, 2, 3, 4, 5, 6, 8 };
            } else {
                outputIndex = new int[] { 0, 2, 3, 4, 8 };
            }
        }
        nrows = alleleEstimateResults.size();
        table = new Object[nrows][];
        numberOfColumns = outputIndex.length;
        colnames = new String[numberOfColumns];
        for (int j = 0; j < numberOfColumns; j++) {
            colnames[j] = columnLabels[outputIndex[j]];
        }
        for (int i = 0; i < nrows; i++) {
            table[i] = new Object[numberOfColumns];
            Object[] result = alleleEstimateResults.get(i);
            for (int j = 0; j < numberOfColumns; j++) {
                table[i][j] = result[outputIndex[j]];
            }
        }
        tableName = new StringBuilder("GLM allele estimates for ");
        tableName.append(dataset.getName());
        comments = new StringBuilder("Marker allele effect estimates\n");
        comments.append("GLM: fixed effect linear model\n");
        comments.append("Data set: ").append(dataset.getName());
        comments.append("\nmodel: trait = mean");
        for (int i = 0; i < theAdapter.getNumberOfFactors(); i++) {
            comments.append(" + ");
            comments.append(theAdapter.getFactorName(i));
        }
        for (int i = 0; i < theAdapter.getNumberOfCovariates(); i++) {
            comments.append(" + ");
            comments.append(theAdapter.getCovariateName(i));
        }
        comments.append(" + marker");
        if (writeOutputToFile) {
            comments.append("\nOutput written to " + blueFile.getPath());
        }
        TableReport alleleEstimateReport = new SimpleTableReport("Allele Estimates", colnames, table);
        resultset.add(new Datum(tableName.toString(), alleleEstimateReport, comments.toString()));
        fireProgress(0);
        return resultset;
    }
