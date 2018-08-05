    public static BufferedReader getUserSolveStream(String name) throws IOException {
        BufferedReader in;
        try {
            URL url = new URL("http://www.spoj.pl/status/" + name.toLowerCase() + "/signedlist/");
            in = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (MalformedURLException e) {
            in = null;
            throw e;
        }
        return in;
    }
