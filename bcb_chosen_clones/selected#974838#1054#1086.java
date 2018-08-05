    public static String encodeFromFile(String filename) throws java.io.IOException, URISyntaxException {
        String encodedData = null;
        Base641.InputStream bis = null;
        File file;
        try {
            URL url = new URL(filename);
            URLConnection conn = url.openConnection();
            file = new File("myfile.doc");
            java.io.InputStream inputStream = (java.io.InputStream) conn.getInputStream();
            FileOutputStream out = new FileOutputStream(file);
            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) out.write(buf, 0, len);
            out.close();
            inputStream.close();
            byte[] buffer = new byte[Math.max((int) (file.length() * 1.4), 40)];
            int length = 0;
            int numBytes = 0;
            bis = new Base641.InputStream(new java.io.BufferedInputStream(new java.io.FileInputStream(file)), Base641.ENCODE);
            while ((numBytes = bis.read(buffer, length, 4096)) >= 0) {
                length += numBytes;
            }
            encodedData = new String(buffer, 0, length, Base641.PREFERRED_ENCODING);
        } catch (java.io.IOException e) {
            throw e;
        } finally {
            try {
                bis.close();
            } catch (Exception e) {
            }
        }
        return encodedData;
    }
