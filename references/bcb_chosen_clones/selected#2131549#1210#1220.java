    private static File createFileFromURL(URL url) throws IOException {
        File tempFile = File.createTempFile("oboFile", ".obo");
        PrintStream ps = new PrintStream(tempFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            ps.println(line);
        }
        ps.close();
        return tempFile;
    }
