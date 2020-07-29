    public boolean checkWebsite(String URL, String content) {
        boolean run = false;
        try {
            URL url = new URL(URL + "?a=" + Math.random());
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (line.contains(content)) {
                    run = true;
                }
            }
        } catch (Exception e) {
            run = false;
        }
        return run;
    }
