    public static void main(String[] args) {
        Hashtable hSwitches = utils.parseCommandLineSwitches(args);
        String sInputFile = utils.getSwitch(hSwitches, "inputFile", "input.txt");
        boolean bSaveModel = Boolean.valueOf(utils.getSwitch(hSwitches, "save", String.valueOf(false))).booleanValue();
        boolean bLoadModel = Boolean.valueOf(utils.getSwitch(hSwitches, "load", String.valueOf(false))).booleanValue();
        boolean bWEKATrain = Boolean.valueOf(utils.getSwitch(hSwitches, "WEKATrain", String.valueOf(false))).booleanValue();
        boolean bWEKATest = Boolean.valueOf(utils.getSwitch(hSwitches, "WEKATest", String.valueOf(false))).booleanValue();
        int iMinNGram = Integer.valueOf(utils.getSwitch(hSwitches, "minNGram", "3")).intValue();
        int iMaxNGram = Integer.valueOf(utils.getSwitch(hSwitches, "maxNGram", "5")).intValue();
        int iMaxDist = Integer.valueOf(utils.getSwitch(hSwitches, "maxDist", "5")).intValue();
        GIFileLoader gi = new GIFileLoader("gi.txt");
        DocumentNGramGraph gPositive = new DocumentNGramSymWinGraph(iMinNGram, iMaxNGram, iMaxDist);
        DocumentNGramGraph gNegative = new DocumentNGramSymWinGraph(iMinNGram, iMaxNGram, iMaxDist);
        DocumentNGramGraph gNeutral = new DocumentNGramSymWinGraph(iMinNGram, iMaxNGram, iMaxDist);
        INSECTFileDB db = new INSECTFileDB("VR_", "models/");
        boolean bLoadedOK = false;
        if (bLoadModel) {
            System.err.print("Loading model...");
            gPositive = (DocumentNGramGraph) db.loadObject("pos", "graphModel");
            gNegative = (DocumentNGramGraph) db.loadObject("neg", "graphModel");
            gNeutral = (DocumentNGramGraph) db.loadObject("neutral", "graphModel");
            bLoadedOK = (gPositive != null) && (gNegative != null) && (gNeutral != null);
            System.err.println("OK.");
        }
        if (!bLoadedOK) {
            System.err.print("Training model...");
            gPositive = new DocumentNGramSymWinGraph(iMinNGram, iMaxNGram, iMaxDist);
            gNegative = new DocumentNGramSymWinGraph(iMinNGram, iMaxNGram, iMaxDist);
            gNeutral = new DocumentNGramSymWinGraph(iMinNGram, iMaxNGram, iMaxDist);
            int iPosCnt = 0, iNegCnt = 0, iNeutralCnt = 0, iOverallCnt = 0;
            for (String sCurSense : gi.hSenseToDefinition.keySet()) {
                int iPol = gi.hSenseToPolarity.get(sCurSense);
                int iHashPos = sCurSense.lastIndexOf("#");
                String sCurWord = (iHashPos < 0) ? sCurSense : sCurSense.substring(0, iHashPos);
                String sDef = gi.hSenseToDefinition.get(sCurSense);
                DocumentNGramGraph gTmp = new DocumentNGramSymWinGraph(iMinNGram, iMaxNGram, iMaxDist);
                int iColonIdx = sDef.lastIndexOf(":");
                if (iColonIdx > -1) gTmp.setDataString(sCurWord + " " + sDef.substring(iColonIdx + 1).replace("\"[|]", " ")); else gTmp.setDataString(sCurWord.replace("\"[|]", " "));
                if (iPol == GIFileLoader.POLARITY_POSITIVE) {
                    gPositive.merge(gTmp, (1.0 - (iPosCnt / ++iPosCnt)));
                } else if (iPol == GIFileLoader.POLARITY_NEGATIVE) {
                    gNegative.merge(gTmp, (1.0 - (iNegCnt / ++iNegCnt)));
                } else {
                    gPositive.degrade(gTmp);
                    gNegative.degrade(gTmp);
                }
                if (++iOverallCnt % 100 == 0) System.err.println("Updated " + String.valueOf(iOverallCnt) + " of " + String.valueOf(gi.hSenseToDefinition.size()) + "...\t");
            }
            System.err.println("Training model...OK.");
            System.err.println(String.format("Pos: %d\tNeg: %d\tNeu: %d", iPosCnt, iNegCnt, iNeutralCnt));
        }
        if (bSaveModel) {
            System.err.print("Saving model...");
            db.saveObject(gPositive, "pos", "graphModel");
            db.saveObject(gNegative, "neg", "graphModel");
            db.saveObject(gNeutral, "neutral", "graphModel");
            System.err.println("Done.");
        }
        if (bWEKATrain) {
            System.out.println("@RELATION opinitionSum\n");
            System.out.println("@ATTRIBUTE posNormValSim NUMERIC\n" + "@ATTRIBUTE negNormValSim NUMERIC\n" + "@ATTRIBUTE neutralNormValSim NUMERIC\n" + "@ATTRIBUTE polarity {pos,neg,neutral}\n\n" + "@DATA");
            Distribution dResults = new Distribution();
            for (String sCurSense : gi.hSenseToDefinition.keySet()) {
                int iHashPos = sCurSense.lastIndexOf("#");
                String sCurWord = (iHashPos < 0) ? sCurSense : sCurSense.substring(0, iHashPos);
                String sDef = gi.hSenseToDefinition.get(sCurSense);
                int iCurPolarity = gi.hSenseToPolarity.get(sCurSense);
                String sCurPolarity = getPolarityStr(iCurPolarity);
                DocumentNGramGraph gTmp = new DocumentNGramSymWinGraph(iMinNGram, iMaxNGram, iMaxDist);
                int iColonIdx = sDef.lastIndexOf(":");
                if (iColonIdx > -1) gTmp.setDataString(sCurWord + " " + sDef.substring(iColonIdx + 1).replace("\"[|]", " ")); else gTmp.setDataString(sCurWord.replace("\"[|]", " "));
                int iPol = gi.hSenseToPolarity.get(sCurSense);
                NGramGraphComparator comp = new NGramGraphComparator();
                Distribution<String> d = new Distribution();
                Distribution<Integer> d2 = new Distribution();
                GraphSimilarity simil = comp.getSimilarityBetween(gTmp, gPositive);
                d.setValue("pos", simil.SizeSimilarity == 0.0 ? 0.0 : simil.ValueSimilarity / simil.SizeSimilarity);
                d2.setValue(GIFileLoader.POLARITY_POSITIVE, simil.SizeSimilarity == 0.0 ? 0.0 : simil.ValueSimilarity / simil.SizeSimilarity);
                simil = comp.getSimilarityBetween(gTmp, gNegative);
                d.setValue("neg", simil.SizeSimilarity == 0.0 ? 0.0 : simil.ValueSimilarity / simil.SizeSimilarity);
                d2.setValue(GIFileLoader.POLARITY_NEGATIVE, simil.SizeSimilarity == 0.0 ? 0.0 : simil.ValueSimilarity / simil.SizeSimilarity);
                if (d2.getKeyOfMaxValue() == iCurPolarity) dResults.increaseValue(sCurPolarity + "Correct", 1.0);
                dResults.increaseValue(sCurPolarity + "All", 1.0);
                System.out.println(String.format("%8.6f,%8.6f,%8.6f,%s", d.getValue("pos"), d.getValue("neg"), d.getValue("neutral"), sCurPolarity));
            }
            System.out.println("Results using SIMPLE SIMILARITY:\n" + dResults.toString());
        }
        LocalWordNetMeaningExtractor l = null;
        try {
            l = new LocalWordNetMeaningExtractor();
        } catch (IOException ex) {
            Logger.getLogger(polarityEstimator.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
        if (bWEKATest) {
            System.out.println("@RELATION opinitionSum\n");
            System.out.println("@ATTRIBUTE posNormValSim NUMERIC\n" + "@ATTRIBUTE negNormValSim NUMERIC\n" + "@ATTRIBUTE neutralNormValSim NUMERIC\n" + "@ATTRIBUTE polarity {pos,neg,neutral}\n\n" + "@DATA");
        }
        String sFullText = utils.loadFileToStringWithNewlines(sInputFile);
        Pattern p = Pattern.compile("\\w+[#]\\w+[#]\\d+");
        Matcher mSeeker = p.matcher(sFullText);
        while (mSeeker.find()) {
            String sSubString = sFullText.substring(mSeeker.start(), mSeeker.end());
            String[] saTokens = sSubString.split("#");
            String sWord = saTokens[0];
            String sPOS = saTokens[1];
            int iSenseNum = Integer.valueOf(saTokens[2]);
            DocumentNGramGraph gTest = new DocumentNGramSymWinGraph(iMinNGram, iMaxNGram, iMaxDist);
            gTest.setDataString(allTextForSense(l, sWord, sPOS, iSenseNum));
            NGramGraphComparator comp = new NGramGraphComparator();
            Distribution<String> d = new Distribution();
            GraphSimilarity simil = comp.getSimilarityBetween(gTest, gPositive);
            d.setValue("pos", simil.SizeSimilarity == 0 ? 0.0 : (simil.ValueSimilarity / simil.SizeSimilarity));
            simil = comp.getSimilarityBetween(gTest, gNegative);
            d.setValue("neg", simil.SizeSimilarity == 0 ? 0.0 : (simil.ValueSimilarity / simil.SizeSimilarity));
            if (!bWEKATest) {
                System.out.println(String.format("%s # %s # %d\n%s\n-->%s", sWord, sPOS, iSenseNum, gTest.getDataString(), d.getKeyOfMaxValue()));
                System.out.println("Similarity Values: \n" + d.toString());
            } else {
                System.out.println(String.format("%8.6f,%8.6f,%8.6f,%8.6s", d.getValue("pos"), d.getValue("neg"), d.getValue("neutral"), "?"));
            }
        }
        ;
    }
