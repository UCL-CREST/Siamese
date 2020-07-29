    private void populateSessionId() throws IOException, java.net.MalformedURLException {
        String general_search_url = "http://agricola.nal.usda.gov/cgi-bin/Pwebrecon.cgi?" + "DB=local&CNT=1&Search_Arg=RNAi&Search_Code=GKEY&STARTDB=AGRIDB";
        String sidString = "", inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader((new URL(general_search_url)).openStream()));
        while ((inputLine = in.readLine()) != null) {
            if (inputLine.startsWith("<INPUT TYPE=HIDDEN NAME=PID VALUE=")) {
                sidString = (inputLine.substring(inputLine.indexOf("PID VALUE=") + 11, inputLine.indexOf(">") - 1));
            }
        }
        sessionId = Integer.parseInt(sidString.trim());
    }
