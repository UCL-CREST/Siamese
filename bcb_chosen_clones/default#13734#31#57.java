    public void createWordTable(String stFilename) {
        String stLine = null;
        String stWord = null;
        double dOccurCount = 0;
        try {
            BufferedReader fl = new BufferedReader(new FileReader(stFilename));
            while ((stLine = fl.readLine()) != null) {
                StringTokenizer line = new StringTokenizer(stLine);
                while (line.hasMoreTokens()) {
                    dNumProcessed++;
                    stWord = line.nextToken();
                    stWord = stWord.toLowerCase();
                    if (freqWords.containsKey(stWord)) {
                        Object temp = freqWords.get(stWord);
                        dOccurCount = ((Double) temp).doubleValue();
                        dOccurCount++;
                        freqWords.put(stWord, new Double(dOccurCount));
                    } else {
                        freqWords.put(stWord, new Double(1));
                    }
                }
            }
            fl.close();
        } catch (IOException e) {
            System.out.println("ERROR READING FILE: " + stFilename);
        }
    }
