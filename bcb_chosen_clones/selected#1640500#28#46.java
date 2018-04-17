    public void load(String fileName) {
        BufferedReader bufReader;
        loaded = false;
        vector.removeAllElements();
        try {
            if (fileName.startsWith("http:")) {
                URL url = new URL(fileName);
                bufReader = new BufferedReader(new InputStreamReader(url.openStream()));
            } else bufReader = new BufferedReader(new FileReader(fileName));
            String inputLine;
            while ((inputLine = bufReader.readLine()) != null) {
                if (listener != null) listener.handleLine(inputLine); else vector.add(inputLine);
            }
            bufReader.close();
            loaded = true;
        } catch (IOException e) {
            errorMsg = e.getMessage();
        }
    }
