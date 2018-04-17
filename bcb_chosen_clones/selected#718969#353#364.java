    private void fetchTree() throws IOException {
        String urlString = BASE_URL + TREE_URL + DATASET_URL + "&family=" + mFamily;
        URL url = new URL(urlString);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String toParse = in.readLine();
        while (in.ready()) {
            String add = in.readLine();
            if (add == null) break;
            toParse += add;
        }
        if (toParse != null && !toParse.startsWith("No tree available")) mProteinTree = new PTree(this, toParse);
    }
