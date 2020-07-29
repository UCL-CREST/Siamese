    public static boolean copy(String source, String dest) {
        int bytes;
        byte array[] = new byte[BUFFER_LEN];
        try {
            InputStream is = new FileInputStream(source);
            OutputStream os = new FileOutputStream(dest);
            while ((bytes = is.read(array, 0, BUFFER_LEN)) > 0) os.write(array, 0, bytes);
            is.close();
            os.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
