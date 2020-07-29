    public static String getTextFileFromURL(String urlName) {
        try {
            StringBuffer textFile = new StringBuffer("");
            String line = null;
            URL url = new URL(urlName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = reader.readLine()) != null) textFile = textFile.append(line + "\n");
            reader.close();
            return textFile.toString();
        } catch (Exception e) {
            Debug.signal(Debug.ERROR, null, "Failed to open " + urlName + ", exception " + e);
            return null;
        }
    }
