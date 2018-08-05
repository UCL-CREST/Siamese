    private static String readURL(URL url) throws IOException {
        BufferedReader in = null;
        StringBuffer s = new StringBuffer();
        try {
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                s.append(str);
            }
        } finally {
            if (in != null) in.close();
        }
        return s.toString();
    }
