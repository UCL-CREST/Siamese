    private boolean getCached(Get g) throws IOException {
        boolean ret = false;
        File f = getCachedFile(g);
        if (f.exists()) {
            InputStream is = null;
            OutputStream os = null;
            try {
                is = new FileInputStream(f);
                os = new FileOutputStream(getDestFile(g));
                int read;
                byte[] buffer = new byte[4096];
                while ((read = is.read(buffer)) > 0) {
                    os.write(buffer, 0, read);
                }
                ret = true;
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
                is = null;
                os = null;
            }
        }
        return ret;
    }
