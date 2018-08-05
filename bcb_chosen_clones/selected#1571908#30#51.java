    public final int wordFrequency(String word) {
        String replWebQuery = webQuery.replaceFirst("WORDREPLACE", word);
        try {
            URL url = new URL(replWebQuery);
            String content = url.toString();
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.matches(nrResultsPattern)) {
                    int fr = matchedLine(inputLine);
                    if (fr >= 0) {
                        return fr;
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
