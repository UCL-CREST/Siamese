    public ScoreModel(URL url) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        list = new ArrayList<ScoreModelItem>();
        map = new HashMap<String, ScoreModelItem>();
        line = in.readLine();
        int n = 1;
        String[] rowAttrib;
        ScoreModelItem item;
        while ((line = in.readLine()) != null) {
            rowAttrib = line.split(";");
            item = new ScoreModelItem(n, Double.valueOf(rowAttrib[3]), Double.valueOf(rowAttrib[4]), Double.valueOf(rowAttrib[2]), Double.valueOf(rowAttrib[5]), rowAttrib[1]);
            list.add(item);
            map.put(item.getHash(), item);
            n++;
        }
        in.close();
    }
