    public static void main(String[] args) {
        paraProc(args);
        CanonicalGFF cgff = new CanonicalGFF(gffFilename);
        CanonicalGFF geneModel = new CanonicalGFF(modelFilename);
        CanonicalGFF transcriptGff = new CanonicalGFF(transcriptFilename);
        TreeMap ksTable1 = getKsTable(ksTable1Filename);
        TreeMap ksTable2 = getKsTable(ksTable2Filename);
        Map intronReadCntMap = new TreeMap();
        Map intronSplicingPosMap = new TreeMap();
        try {
            BufferedReader fr = new BufferedReader(new FileReader(inFilename));
            while (fr.ready()) {
                String line = fr.readLine();
                if (line.startsWith("#")) continue;
                String tokens[] = line.split("\t");
                String chr = tokens[0];
                int start = Integer.parseInt(tokens[1]);
                int stop = Integer.parseInt(tokens[2]);
                GenomeInterval intron = new GenomeInterval(chr, start, stop);
                int readCnt = Integer.parseInt(tokens[3]);
                intronReadCntMap.put(intron, readCnt);
                String splicingMapStr = tokens[4];
                Map splicingMap = getSplicingMap(splicingMapStr);
                intronSplicingPosMap.put(intron, splicingMap);
            }
            fr.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
        double[] hdCDF = getHdCdf(readLength, minimumOverlap);
        try {
            FileWriter fw = new FileWriter(outFilename);
            for (Iterator intronIterator = intronReadCntMap.keySet().iterator(); intronIterator.hasNext(); ) {
                GenomeInterval intron = (GenomeInterval) intronIterator.next();
                int readCnt = ((Integer) intronReadCntMap.get(intron)).intValue();
                TreeMap splicingMap = (TreeMap) intronSplicingPosMap.get(intron);
                Object ksInfoArray[] = distributionAccepter((TreeMap) splicingMap.clone(), readCnt, hdCDF, ksTable1, ksTable2);
                boolean ksAccepted = (Boolean) ksInfoArray[0];
                double testK = (Double) ksInfoArray[1];
                double standardK1 = (Double) ksInfoArray[2];
                double standardK2 = (Double) ksInfoArray[3];
                int positionCnt = splicingMap.size();
                Object modelInfoArray[] = getModelAgreedSiteCnt(intron, cgff, geneModel, transcriptGff);
                int modelAgreedSiteCnt = (Integer) modelInfoArray[0];
                int maxAgreedTransSiteCnt = (Integer) modelInfoArray[1];
                boolean containedBySomeGene = (Boolean) modelInfoArray[2];
                int numIntersectingGenes = (Integer) modelInfoArray[3];
                int distance = intron.getStop() - intron.getStart();
                fw.write(intron.getChr() + ":" + intron.getStart() + ".." + intron.getStop() + "\t" + distance + "\t" + readCnt + "\t" + splicingMap + "\t" + probabilityEvaluation(readLength, distance, readCnt, splicingMap, positionCnt) + "\t" + ksAccepted + "\t" + testK + "\t" + standardK1 + "\t" + standardK2 + "\t" + positionCnt + "\t" + modelAgreedSiteCnt + "\t" + maxAgreedTransSiteCnt + "\t" + containedBySomeGene + "\t" + numIntersectingGenes + "\n");
            }
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
