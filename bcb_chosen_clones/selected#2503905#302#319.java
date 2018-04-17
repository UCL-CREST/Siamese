    private void copyTemplate(String resource, OutputStream outputStream) throws IOException {
        URL url = Tools.getResource(resource);
        if (url == null) {
            throw new IOException("could not find resource");
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName("UTF-8")));
        String line = null;
        do {
            line = reader.readLine();
            if (line != null) {
                writer.write(line);
                writer.newLine();
            }
        } while (line != null);
        reader.close();
        writer.close();
    }
