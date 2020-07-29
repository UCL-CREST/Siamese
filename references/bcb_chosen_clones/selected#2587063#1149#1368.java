    private void analyseCorpus(final IStatusDisplayer fStatus) {
        final String sDistrosFile = "Distros.tmp";
        final String sSymbolsFile = "Symbols.tmp";
        Chunker = new EntropyChunker();
        int Levels = 2;
        sgOverallGraph = new SymbolicGraph(1, Levels);
        siIndex = new SemanticIndex(sgOverallGraph);
        try {
            siIndex.MeaningExtractor = new LocalWordNetMeaningExtractor();
        } catch (IOException ioe) {
            siIndex.MeaningExtractor = null;
        }
        try {
            DocumentSet dsSet = new DocumentSet(FilePathEdt.getText(), 1.0);
            dsSet.createSets(true, (double) 100 / 100);
            int iCurCnt, iTotal;
            String sFile = "";
            Iterator iIter = dsSet.getTrainingSet().iterator();
            iTotal = dsSet.getTrainingSet().size();
            if (iTotal == 0) {
                appendToLog("No input documents.\n");
                appendToLog("======DONE=====\n");
                return;
            }
            appendToLog("Training chunker...");
            Chunker.train(dsSet.toFilenameSet(DocumentSet.FROM_WHOLE_SET));
            appendToLog("Setting delimiters...");
            setDelimiters(Chunker.getDelimiters());
            iCurCnt = 0;
            cdDoc = new DistributionDocument[Levels];
            for (int iCnt = 0; iCnt < Levels; iCnt++) cdDoc[iCnt] = new DistributionDocument(1, MinLevel + iCnt);
            fStatus.setVisible(true);
            ThreadList t = new ThreadList(Runtime.getRuntime().availableProcessors() + 1);
            appendToLog("(Pass 1/3) Loading files..." + sFile);
            TreeSet tsOverallSymbols = new TreeSet();
            while (iIter.hasNext()) {
                sFile = ((CategorizedFileEntry) iIter.next()).getFileName();
                fStatus.setStatus("(Pass 1/3) Loading file..." + sFile, (double) iCurCnt / iTotal);
                final DistributionDocument[] cdDocArg = cdDoc;
                final String sFileArg = sFile;
                for (int iCnt = 0; iCnt < cdDoc.length; iCnt++) {
                    final int iCntArg = iCnt;
                    while (!t.addThreadFor(new Runnable() {

                        public void run() {
                            if (!RightToLeftText) cdDocArg[iCntArg].loadDataStringFromFile(sFileArg, false); else {
                                cdDocArg[iCntArg].setDataString(utils.reverseString(utils.loadFileToString(sFileArg)), iCntArg, false);
                            }
                        }
                    })) Thread.yield();
                }
                try {
                    t.waitUntilCompletion();
                } catch (InterruptedException ex) {
                    ex.printStackTrace(System.err);
                    appendToLog("Interrupted...");
                    sgOverallGraph.removeNotificationListener();
                    return;
                }
                sgOverallGraph.setDataString(((new StringBuffer().append((char) StreamTokenizer.TT_EOF))).toString());
                sgOverallGraph.loadFromFile(sFile);
                fStatus.setStatus("Loaded file..." + sFile, (double) ++iCurCnt / iTotal);
                Thread.yield();
            }
            Set sSymbols = null;
            File fPreviousSymbols = new File(sSymbolsFile);
            boolean bSymbolsLoadedOK = false;
            if (fPreviousSymbols.exists()) {
                System.err.println("ATTENTION: Using previous symbols...");
                try {
                    FileInputStream fis = new FileInputStream(fPreviousSymbols);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    sSymbols = (Set) ois.readObject();
                    ois.close();
                    bSymbolsLoadedOK = true;
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace(System.err);
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace(System.err);
                }
            }
            if (!bSymbolsLoadedOK) sSymbols = getSymbolsByProbabilities(sgOverallGraph.getDataString(), fStatus);
            int iMinSymbolSize = Integer.MAX_VALUE;
            int iMaxSymbolSize = Integer.MIN_VALUE;
            Iterator iSymbol = sSymbols.iterator();
            while (iSymbol.hasNext()) {
                String sCurSymbol = (String) iSymbol.next();
                if (iMaxSymbolSize < sCurSymbol.length()) iMaxSymbolSize = sCurSymbol.length();
                if (iMinSymbolSize > sCurSymbol.length()) iMinSymbolSize = sCurSymbol.length();
            }
            try {
                FileOutputStream fos = new FileOutputStream(sSymbolsFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(sSymbols);
                oos.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
            appendToLog("(Pass 2/3) Determining symbol distros per n-gram size...");
            iIter = dsSet.getTrainingSet().iterator();
            iTotal = dsSet.getTrainingSet().size();
            if (iTotal == 0) {
                appendToLog("No input documents.\n");
                appendToLog("======DONE=====\n");
                return;
            }
            iCurCnt = 0;
            Distribution dSymbolsPerSize = new Distribution();
            Distribution dNonSymbolsPerSize = new Distribution();
            Distribution dSymbolSizes = new Distribution();
            File fPreviousRun = new File(sDistrosFile);
            boolean bDistrosLoadedOK = false;
            if (fPreviousRun.exists()) {
                System.err.println("ATTENTION: Using previous distros...");
                try {
                    FileInputStream fis = new FileInputStream(fPreviousRun);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    dSymbolsPerSize = (Distribution) ois.readObject();
                    dNonSymbolsPerSize = (Distribution) ois.readObject();
                    dSymbolSizes = (Distribution) ois.readObject();
                    ois.close();
                    bDistrosLoadedOK = true;
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace(System.err);
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                    dSymbolsPerSize = new Distribution();
                    dNonSymbolsPerSize = new Distribution();
                    dSymbolSizes = new Distribution();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace(System.err);
                    dSymbolsPerSize = new Distribution();
                    dNonSymbolsPerSize = new Distribution();
                    dSymbolSizes = new Distribution();
                }
            }
            if (!bDistrosLoadedOK) while (iIter.hasNext()) {
                fStatus.setStatus("(Pass 2/3) Parsing file..." + sFile, (double) iCurCnt++ / iTotal);
                sFile = ((CategorizedFileEntry) iIter.next()).getFileName();
                String sDataString = "";
                try {
                    ByteArrayOutputStream bsOut = new ByteArrayOutputStream();
                    FileInputStream fiIn = new FileInputStream(sFile);
                    int iData = 0;
                    while ((iData = fiIn.read()) > -1) bsOut.write(iData);
                    sDataString = bsOut.toString();
                } catch (IOException ioe) {
                    ioe.printStackTrace(System.err);
                }
                final Distribution dSymbolsPerSizeArg = dSymbolsPerSize;
                final Distribution dNonSymbolsPerSizeArg = dNonSymbolsPerSize;
                final Distribution dSymbolSizesArg = dSymbolSizes;
                final String sDataStringArg = sDataString;
                final Set sSymbolsArg = sSymbols;
                for (int iSymbolSize = iMinSymbolSize; iSymbolSize <= iMaxSymbolSize; iSymbolSize++) {
                    final int iSymbolSizeArg = iSymbolSize;
                    while (!t.addThreadFor(new Runnable() {

                        public void run() {
                            NGramDocument ndCur = new NGramDocument(iSymbolSizeArg, iSymbolSizeArg, 1, iSymbolSizeArg, iSymbolSizeArg);
                            ndCur.setDataString(sDataStringArg);
                            int iSymbolCnt = 0;
                            int iNonSymbolCnt = 0;
                            Iterator iExtracted = ndCur.getDocumentGraph().getGraphLevel(0).getVertexSet().iterator();
                            while (iExtracted.hasNext()) {
                                String sCur = ((Vertex) iExtracted.next()).toString();
                                if (sSymbolsArg.contains(sCur)) {
                                    iSymbolCnt++;
                                    synchronized (dSymbolSizesArg) {
                                        dSymbolSizesArg.setValue(sCur.length(), dSymbolSizesArg.getValue(sCur.length()) + 1.0);
                                    }
                                } else iNonSymbolCnt++;
                            }
                            synchronized (dSymbolsPerSizeArg) {
                                dSymbolsPerSizeArg.setValue(iSymbolSizeArg, dSymbolsPerSizeArg.getValue(iSymbolSizeArg) + iSymbolCnt);
                            }
                            synchronized (dNonSymbolsPerSizeArg) {
                                dNonSymbolsPerSizeArg.setValue(iSymbolSizeArg, dNonSymbolsPerSizeArg.getValue(iSymbolSizeArg) + iNonSymbolCnt);
                            }
                        }
                    })) Thread.yield();
                }
            }
            if (!bDistrosLoadedOK) try {
                t.waitUntilCompletion();
                try {
                    FileOutputStream fos = new FileOutputStream(sDistrosFile);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(dSymbolsPerSize);
                    oos.writeObject(dNonSymbolsPerSize);
                    oos.writeObject(dSymbolSizes);
                    oos.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace(System.err);
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
            } catch (InterruptedException ex) {
                appendToLog("Interrupted...");
                sgOverallGraph.removeNotificationListener();
                return;
            }
            appendToLog("\n(Pass 3/3) Determining optimal n-gram range...\n");
            NGramSizeEstimator nseEstimator = new NGramSizeEstimator(dSymbolsPerSize, dNonSymbolsPerSize);
            IntegerPair p = nseEstimator.getOptimalRange();
            appendToLog("\nProposed n-gram sizes:" + p.first() + "," + p.second());
            fStatus.setStatus("Determining optimal distance...", 0.0);
            DistanceEstimator de = new DistanceEstimator(dSymbolsPerSize, dNonSymbolsPerSize, nseEstimator);
            int iBestDist = de.getOptimalDistance(1, nseEstimator.getMaxRank() * 2, p.first(), p.second());
            fStatus.setStatus("Determining optimal distance...", 1.0);
            appendToLog("\nOptimal distance:" + iBestDist);
            appendToLog("======DONE=====\n");
        } finally {
            sgOverallGraph.removeNotificationListener();
        }
    }
