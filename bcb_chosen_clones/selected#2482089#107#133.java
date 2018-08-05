    private boolean copy_to_file_io(File src, File dst) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(src);
            is = new BufferedInputStream(is);
            os = new FileOutputStream(dst);
            os = new BufferedOutputStream(os);
            byte buffer[] = new byte[1024 * 64];
            int read;
            while ((read = is.read(buffer)) > 0) {
                os.write(buffer, 0, read);
            }
            return true;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                Debug.debug(e);
            }
            try {
                if (os != null) os.close();
            } catch (IOException e) {
                Debug.debug(e);
            }
        }
    }
