    private List<String> readUrl(URL url) throws IOException {
        List<String> lines = new ArrayList<String>();
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String str;
        while ((str = in.readLine()) != null) {
            lines.add(str);
        }
        in.close();
        return lines;
    }
