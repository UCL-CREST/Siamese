    public String getMatches() {
        StringBuilder builder = new StringBuilder("");
        try {
            URL url = new URL(LIVE_SCORE_URL);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                builder.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
