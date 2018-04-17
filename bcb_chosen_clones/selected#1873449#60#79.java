    public static String readUrl(String urlString) {
        try {
            java.net.URL url = new java.net.URL(urlString);
            BufferedReader br = null;
            if (url != null) {
                br = new BufferedReader(new InputStreamReader(url.openStream()));
            }
            StringBuffer fileString = new StringBuffer();
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                fileString.append(line + "\n");
            }
            return fileString.toString();
        } catch (Exception e) {
            return null;
        }
    }
