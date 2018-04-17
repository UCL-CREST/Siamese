    private void copyFile(File src_file, File dest_file) {
        InputStream src_stream = null;
        OutputStream dest_stream = null;
        try {
            int b;
            src_stream = new BufferedInputStream(new FileInputStream(src_file));
            dest_stream = new BufferedOutputStream(new FileOutputStream(dest_file));
            while ((b = src_stream.read()) != -1) dest_stream.write(b);
        } catch (Exception e) {
            XRepository.getLogger().warning(this, "Error on copying the plugin file!");
            XRepository.getLogger().warning(this, e);
        } finally {
            try {
                src_stream.close();
                dest_stream.close();
            } catch (Exception ex2) {
            }
        }
    }
