    public String getScore(int id) {
        String title = null;
        try {
            URL url = new URL(BASE_URL + id + ".html");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("<title>")) {
                    title = line.substring(line.indexOf("<title>") + 7, line.indexOf("</title>"));
                    title = title.substring(0, title.indexOf("|")).trim();
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return title;
    }
