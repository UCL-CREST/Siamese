    public static void main(String[] args) throws IOException {
        String paramFileName = args[0];
        BufferedReader inFile_params = new BufferedReader(new FileReader(paramFileName));
        String cands_fileName = (inFile_params.readLine().split("\\s+"))[0];
        String alignSrcCand_phrasal_fileName = (inFile_params.readLine().split("\\s+"))[0];
        String alignSrcCand_word_fileName = (inFile_params.readLine().split("\\s+"))[0];
        String source_fileName = (inFile_params.readLine().split("\\s+"))[0];
        String trainSrc_fileName = (inFile_params.readLine().split("\\s+"))[0];
        String trainTgt_fileName = (inFile_params.readLine().split("\\s+"))[0];
        String trainAlign_fileName = (inFile_params.readLine().split("\\s+"))[0];
        String alignCache_fileName = (inFile_params.readLine().split("\\s+"))[0];
        String alignmentsType = "AlignmentGrids";
        int maxCacheSize = 1000;
        inFile_params.close();
        int numSentences = countLines(source_fileName);
        InputStream inStream_src = new FileInputStream(new File(source_fileName));
        BufferedReader srcFile = new BufferedReader(new InputStreamReader(inStream_src, "utf8"));
        String[] srcSentences = new String[numSentences];
        for (int i = 0; i < numSentences; ++i) {
            srcSentences[i] = srcFile.readLine();
        }
        srcFile.close();
        println("Creating src vocabulary @ " + (new Date()));
        srcVocab = new Vocabulary();
        int[] sourceWordsSentences = Vocabulary.initializeVocabulary(trainSrc_fileName, srcVocab, true);
        int numSourceWords = sourceWordsSentences[0];
        int numSourceSentences = sourceWordsSentences[1];
        println("Reading src corpus @ " + (new Date()));
        srcCorpusArray = SuffixArrayFactory.createCorpusArray(trainSrc_fileName, srcVocab, numSourceWords, numSourceSentences);
        println("Creating src SA @ " + (new Date()));
        srcSA = SuffixArrayFactory.createSuffixArray(srcCorpusArray, maxCacheSize);
        println("Creating tgt vocabulary @ " + (new Date()));
        tgtVocab = new Vocabulary();
        int[] targetWordsSentences = Vocabulary.initializeVocabulary(trainTgt_fileName, tgtVocab, true);
        int numTargetWords = targetWordsSentences[0];
        int numTargetSentences = targetWordsSentences[1];
        println("Reading tgt corpus @ " + (new Date()));
        tgtCorpusArray = SuffixArrayFactory.createCorpusArray(trainTgt_fileName, tgtVocab, numTargetWords, numTargetSentences);
        println("Creating tgt SA @ " + (new Date()));
        tgtSA = SuffixArrayFactory.createSuffixArray(tgtCorpusArray, maxCacheSize);
        int trainingSize = srcCorpusArray.getNumSentences();
        if (trainingSize != tgtCorpusArray.getNumSentences()) {
            throw new RuntimeException("Source and target corpora have different number of sentences. This is bad.");
        }
        println("Reading alignment data @ " + (new Date()));
        alignments = null;
        if ("AlignmentArray".equals(alignmentsType)) {
            alignments = SuffixArrayFactory.createAlignments(trainAlign_fileName, srcSA, tgtSA);
        } else if ("AlignmentGrids".equals(alignmentsType) || "AlignmentsGrid".equals(alignmentsType)) {
            alignments = new AlignmentGrids(new Scanner(new File(trainAlign_fileName)), srcCorpusArray, tgtCorpusArray, trainingSize, true);
        } else if ("MemoryMappedAlignmentGrids".equals(alignmentsType)) {
            alignments = new MemoryMappedAlignmentGrids(trainAlign_fileName, srcCorpusArray, tgtCorpusArray);
        }
        if (!fileExists(alignCache_fileName)) {
            alreadyResolved_srcSet = new HashMap<String, TreeSet<Integer>>();
            alreadyResolved_tgtSet = new HashMap<String, TreeSet<Integer>>();
        } else {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(alignCache_fileName));
                alreadyResolved_srcSet = (HashMap<String, TreeSet<Integer>>) in.readObject();
                alreadyResolved_tgtSet = (HashMap<String, TreeSet<Integer>>) in.readObject();
                in.close();
            } catch (FileNotFoundException e) {
                System.err.println("FileNotFoundException in AlignCandidates.main(String[]): " + e.getMessage());
                System.exit(99901);
            } catch (IOException e) {
                System.err.println("IOException in AlignCandidates.main(String[]): " + e.getMessage());
                System.exit(99902);
            } catch (ClassNotFoundException e) {
                System.err.println("ClassNotFoundException in AlignCandidates.main(String[]): " + e.getMessage());
                System.exit(99904);
            }
        }
        println("Processing candidates @ " + (new Date()));
        PrintWriter outFile_alignSrcCand_phrasal = new PrintWriter(alignSrcCand_phrasal_fileName);
        PrintWriter outFile_alignSrcCand_word = new PrintWriter(alignSrcCand_word_fileName);
        InputStream inStream_cands = new FileInputStream(new File(cands_fileName));
        BufferedReader candsFile = new BufferedReader(new InputStreamReader(inStream_cands, "utf8"));
        String line = "";
        String cand = "";
        line = candsFile.readLine();
        int countSatisfied = 0;
        int countAll = 0;
        int countSatisfied_sizeOne = 0;
        int countAll_sizeOne = 0;
        int prev_i = -1;
        String srcSent = "";
        String[] srcWords = null;
        int candsRead = 0;
        int C50count = 0;
        while (line != null) {
            ++candsRead;
            println("Read candidate on line #" + candsRead);
            int i = toInt((line.substring(0, line.indexOf("|||"))).trim());
            if (i != prev_i) {
                srcSent = srcSentences[i];
                srcWords = srcSent.split("\\s+");
                prev_i = i;
                println("New value for i: " + i + " seen @ " + (new Date()));
                C50count = 0;
            } else {
                ++C50count;
            }
            line = (line.substring(line.indexOf("|||") + 3)).trim();
            cand = (line.substring(0, line.indexOf("|||"))).trim();
            cand = cand.substring(cand.indexOf(" ") + 1, cand.length() - 1);
            JoshuaDerivationTree DT = new JoshuaDerivationTree(cand, 0);
            String candSent = DT.toSentence();
            String[] candWords = candSent.split("\\s+");
            String alignSrcCand = DT.alignments();
            outFile_alignSrcCand_phrasal.println(alignSrcCand);
            println("  i = " + i + ", alignSrcCand: " + alignSrcCand);
            String alignSrcCand_res = "";
            String[] linksSrcCand = alignSrcCand.split("\\s+");
            for (int k = 0; k < linksSrcCand.length; ++k) {
                String link = linksSrcCand[k];
                if (link.indexOf(',') == -1) {
                    alignSrcCand_res += " " + link.replaceFirst("--", "-");
                } else {
                    alignSrcCand_res += " " + resolve(link, srcWords, candWords);
                }
            }
            alignSrcCand_res = alignSrcCand_res.trim();
            println("  i = " + i + ", alignSrcCand_res: " + alignSrcCand_res);
            outFile_alignSrcCand_word.println(alignSrcCand_res);
            if (C50count == 50) {
                println("50C @ " + (new Date()));
                C50count = 0;
            }
            line = candsFile.readLine();
        }
        outFile_alignSrcCand_phrasal.close();
        outFile_alignSrcCand_word.close();
        candsFile.close();
        println("Finished processing candidates @ " + (new Date()));
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(alignCache_fileName));
            out.writeObject(alreadyResolved_srcSet);
            out.writeObject(alreadyResolved_tgtSet);
            out.flush();
            out.close();
        } catch (IOException e) {
            System.err.println("IOException in AlignCandidates.main(String[]): " + e.getMessage());
            System.exit(99902);
        }
    }
