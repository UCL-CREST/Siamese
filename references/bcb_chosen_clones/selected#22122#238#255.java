    public static String URLtoString(URL url) throws IOException {
        String xml = null;
        if (url != null) {
            URLConnection con = url.openConnection();
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("User-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
            InputStream is = con.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] bytes = new byte[512];
            for (int i = is.read(bytes, 0, 512); i != -1; i = is.read(bytes, 0, 512)) {
                buffer.write(bytes, 0, i);
            }
            xml = new String(buffer.toByteArray());
            is.close();
            buffer.close();
        }
        return xml;
    }
