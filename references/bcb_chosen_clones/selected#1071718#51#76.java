    private boolean loadSource(URL url) {
        if (url == null) {
            if (sourceURL != null) {
                sourceCodeLinesList.clear();
            }
            return false;
        } else {
            if (url.equals(sourceURL)) {
                return true;
            } else {
                sourceCodeLinesList.clear();
                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                    String line;
                    while ((line = br.readLine()) != null) {
                        sourceCodeLinesList.addElement(line.replaceAll("\t", "   "));
                    }
                    br.close();
                    return true;
                } catch (IOException e) {
                    System.err.println("Could not load source at " + url);
                    return false;
                }
            }
        }
    }
