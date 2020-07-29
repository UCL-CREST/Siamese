    public PageLoader(String pageAddress) throws Exception {
        URL url = new URL(pageAddress);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        inputLine = "";
        while (in.ready()) {
            inputLine = inputLine + in.readLine();
        }
        in.close();
    }
