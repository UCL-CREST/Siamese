    public void createNonPreprocessedWordVector() {
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
                    unsortedTop50.addElement(stWord);
                }
            }
            fl.close();
        } catch (IOException e) {
            System.out.println("ERROR READING FILE: " + stFilename);
        }
    }
