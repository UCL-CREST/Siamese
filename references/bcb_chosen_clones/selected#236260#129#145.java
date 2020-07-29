    private List<String> readLines(String filename) {
        List<String> lines = new ArrayList<String>();
        URL url = Util.getResource(filename);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            while (true) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return lines;
    }
