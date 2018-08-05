    public static boolean dump(File source, File target) {
        boolean done = false;
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(source));
            OutputStream os = new BufferedOutputStream(new FileOutputStream(target));
            while (is.available() > 0) {
                os.write(is.read());
            }
            os.flush();
            os.close();
            is.close();
            return true;
        } catch (IOException e) {
        }
        return done;
    }
