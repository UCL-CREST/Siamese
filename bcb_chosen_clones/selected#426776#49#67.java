    String chooseHGVersion(String version) {
        String line = "";
        try {
            URL connectURL = new URL("http://genome.ucsc.edu/cgi-bin/hgGateway?db=" + version);
            InputStream urlStream = connectURL.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlStream));
            while ((line = reader.readLine()) != null) {
                if (line.indexOf("hgsid") != -1) {
                    line = line.substring(line.indexOf("hgsid"));
                    line = line.substring(line.indexOf("VALUE=\"") + 7);
                    line = line.substring(0, line.indexOf("\""));
                    return line;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;
    }
