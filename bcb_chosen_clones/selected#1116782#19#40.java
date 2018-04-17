    public Stopper(String stopWordsFile) {
        try {
            BufferedReader br = null;
            FileReader fr = null;
            if (stopWordsFile.startsWith("http")) {
                URL url = new URL(stopWordsFile);
                br = new BufferedReader(new InputStreamReader(url.openStream()));
            } else {
                fr = new FileReader(new File(stopWordsFile));
                br = new BufferedReader(fr);
            }
            String line = null;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                stopWords.put(line, "");
            }
            fr.close();
        } catch (Exception e) {
            System.out.println("Stopwords not Found");
            return;
        }
    }
