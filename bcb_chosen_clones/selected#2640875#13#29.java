    public static MMissing load(URL url) throws IOException {
        MMissing ret = new MMissing();
        InputStream is = url.openStream();
        try {
            Reader r = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(r);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    ret.add(line);
                }
            }
            return ret;
        } finally {
            is.close();
        }
    }
