    public static URL getIconURLForUser(String id) {
        try {
            URL url = new URL("http://profiles.yahoo.com/" + id);
            BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
            String input = null;
            while ((input = r.readLine()) != null) {
                if (input.indexOf("<a href=\"") < 0) continue;
                if (input.indexOf("<img src=\"") < 0) continue;
                if (input.indexOf("<a href=\"") > input.indexOf("<img src=\"")) continue;
                String href = input.substring(input.indexOf("<a href=\"") + 9);
                String src = input.substring(input.indexOf("<img src=\"") + 10);
                if (href.indexOf("\"") < 0) continue;
                if (src.indexOf("\"") < 0) continue;
                href = href.substring(0, href.indexOf("\""));
                src = src.substring(0, src.indexOf("\""));
                if (href.equals(src)) {
                    return new URL(href);
                }
            }
        } catch (IOException e) {
        }
        URL toReturn = null;
        try {
            toReturn = new URL("http://us.i1.yimg.com/us.yimg.com/i/ppl/no_photo.gif");
        } catch (MalformedURLException e) {
            Debug.assrt(false);
        }
        return toReturn;
    }
