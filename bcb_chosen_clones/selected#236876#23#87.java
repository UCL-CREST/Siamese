    public void process(String src, String dest) {
        try {
            KanjiDAO kanjiDAO = KanjiDAOFactory.getDAO();
            MorphologicalAnalyzer mecab = MorphologicalAnalyzer.getInstance();
            if (mecab.isActive()) {
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(src), "UTF8"));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dest), "UTF8"));
                String line;
                bw.write("// // // \r\n$title=\r\n$singer=\r\n$id=\r\n\r\n + _______ // 0 0 0 0 0 0 0\r\n\r\n");
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    String segment[] = line.split("//");
                    String japanese = null;
                    String english = null;
                    if (segment.length > 1) english = segment[1].trim();
                    if (segment.length > 0) japanese = segment[0].trim().replaceAll(" ", "_");
                    boolean first = true;
                    if (japanese != null) {
                        ArrayList<ExtractedWord> wordList = mecab.extractWord(japanese);
                        Iterator<ExtractedWord> iter = wordList.iterator();
                        while (iter.hasNext()) {
                            ExtractedWord word = iter.next();
                            if (first) {
                                first = false;
                                bw.write("*");
                            } else bw.write(" ");
                            if (word.isParticle) bw.write("- "); else bw.write("+ ");
                            if (!word.original.equals(word.reading)) {
                                System.out.println("--> " + JapaneseString.toRomaji(word.original) + " / " + JapaneseString.toRomaji(word.reading));
                                KReading[] kr = ReadingAnalyzer.analyzeReadingStub(word.original, word.reading, kanjiDAO);
                                if (kr != null) {
                                    for (int i = 0; i < kr.length; i++) {
                                        if (i > 0) bw.write(" ");
                                        bw.write(kr[i].kanji);
                                        if (kr[i].type != KReading.KANA) {
                                            bw.write("|");
                                            bw.write(kr[i].reading);
                                        }
                                    }
                                } else {
                                    bw.write(word.original);
                                    bw.write("|");
                                    bw.write(word.reading);
                                }
                            } else {
                                bw.write(word.original);
                            }
                            bw.write(" // \r\n");
                        }
                        if (english != null) {
                            bw.write(english);
                            bw.write("\r\n");
                        }
                        bw.write("\r\n");
                    }
                }
                br.close();
                bw.close();
            } else {
                System.out.println("Mecab couldn't be initialized");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
