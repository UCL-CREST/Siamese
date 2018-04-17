    private static boolean DownloadDB() {
        URL url = null;
        BufferedWriter inWriter = null;
        String line;
        try {
            url = new URL(URL);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            inWriter = new BufferedWriter(new FileWriter(InFileName));
            while ((line = reader.readLine()) != null) {
                inWriter.write(line);
                inWriter.newLine();
            }
            inWriter.close();
        } catch (Exception e) {
            try {
                inWriter.close();
            } catch (IOException ignored) {
            }
            e.printStackTrace();
            return false;
        }
        return true;
    }
