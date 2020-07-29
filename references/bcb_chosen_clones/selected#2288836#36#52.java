    public static String getStringFromAFileAtURL(String anURL) {
        String htmlCode = "<html><body></body></html>";
        try {
            URL url = new URL(anURL);
            URLConnection urlConnection = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine = "";
            htmlCode = "";
            while ((inputLine = in.readLine()) != null) htmlCode += inputLine + "\n";
            in.close();
        } catch (Exception e) {
            logs.info("URLResolver : lien mort");
            JOptionPane.showMessageDialog(null, "lien mort");
            return "<html><body></body></html>";
        }
        return checkXMLTagsLowerCase(htmlCode);
    }
