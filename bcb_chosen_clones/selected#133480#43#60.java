    public static String loadResource(String resource) {
        URL url = ClassLoader.getSystemResource("resources/" + resource);
        StringBuffer buffer = new StringBuffer();
        if (url == null) {
            ErrorMessage.handle(new NullPointerException("URL for resources/" + resource + " not found"));
        } else {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }
