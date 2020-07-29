    private String load(URL url) {
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buf = new StringBuffer();
            while (r.ready()) {
                buf.append(r.readLine()).append('\n');
            }
            r.close();
            return buf.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
