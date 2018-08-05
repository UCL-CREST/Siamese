    public static void getWordsFromFile(File file, int documentNo) throws IOException {
        String sentence = "";
        documentWords = new HashMap<String, String>();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = null;
        try {
            isr = new InputStreamReader(fis, "8859_9");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(isr);
        while ((sentence = bufferedReader.readLine()) != null) {
            StringTokenizer tokenizer = new StringTokenizer(sentence, " ");
            while (tokenizer.hasMoreTokens()) addWord(tokenizer.nextToken());
        }
        bufferedReader.close();
        isr.close();
        fis.close();
    }
