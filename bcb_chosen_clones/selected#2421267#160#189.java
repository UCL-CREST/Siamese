    private String GetStringFromURL(String URL) {
        InputStream in = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferedReader = null;
        String outstring = "";
        try {
            java.net.URL url = new java.net.URL(URL);
            in = url.openStream();
            inputStreamReader = new InputStreamReader(in);
            bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer out = new StringBuffer("");
            String nextLine;
            String newline = System.getProperty("line.separator");
            while ((nextLine = bufferedReader.readLine()) != null) {
                out.append(nextLine);
                out.append(newline);
            }
            outstring = new String(out);
        } catch (IOException e) {
            System.out.println("Failed to read from " + URL);
            outstring = "";
        } finally {
            try {
                bufferedReader.close();
                inputStreamReader.close();
            } catch (Exception e) {
            }
        }
        return outstring;
    }
