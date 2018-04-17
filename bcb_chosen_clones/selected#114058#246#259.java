    public static String getWebPage(URL urlObj) {
        try {
            String content = "";
            InputStreamReader is = new InputStreamReader(urlObj.openStream());
            BufferedReader reader = new BufferedReader(is);
            String line;
            while ((line = reader.readLine()) != null) {
                content += line;
            }
            return content;
        } catch (IOException e) {
            throw new Error("The page " + dbg.quote(urlObj.toString()) + "could not be retrieved." + "\nThis is could be caused by a number of things:" + "\n" + "\n  - the computer hosting the web page you want is down, or has returned an error" + "\n  - your computer does not have Internet access" + "\n  - the heat death of the universe has occurred, taking down all web servers with it");
        }
    }
