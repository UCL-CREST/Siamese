    void addDataFromURL(URL theurl) {
        String line;
        InputStream in = null;
        try {
            in = theurl.openStream();
            BufferedReader data = new BufferedReader(new InputStreamReader(in));
            while ((line = data.readLine()) != null) {
                thetext.append(line + "\n");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            thetext.append(theurl.toString());
        }
        try {
            in.close();
        } catch (Exception e) {
        }
    }
