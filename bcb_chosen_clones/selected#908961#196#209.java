    private String retrieveTemplate() throws Exception {
        if (cachedTemplate == null) {
            final URL url = new URL(blogEditor.getBlogInfo().getBlogUrl());
            final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            final StringBuilder result = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();
            cachedTemplate = result.toString();
        }
        return cachedTemplate;
    }
