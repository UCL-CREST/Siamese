    public static BufferedReader getUserInfoStream(String name) throws IOException {
        BufferedReader in;
        try {
            URL url = new URL("http://www.spoj.pl/users/" + name.toLowerCase() + "/");
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (MalformedURLException e) {
            in = null;
            throw e;
        }
        return in;
    }
