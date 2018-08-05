    public static void writeFromURL(String urlstr, String filename) throws Exception {
        URL url = new URL(urlstr);
        InputStream in = url.openStream();
        BufferedReader bf = null;
        StringBuffer sb = new StringBuffer();
        try {
            bf = new BufferedReader(new InputStreamReader(in, "latin1"));
            String s;
            while (true) {
                s = bf.readLine();
                if (s != null) {
                    sb.append(s);
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            bf.close();
        }
        writeRawBytes(sb.toString(), filename);
    }
