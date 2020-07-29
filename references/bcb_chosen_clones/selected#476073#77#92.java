    public void copyFile(File from, File to) {
        try {
            InputStream in = new FileInputStream(from);
            OutputStream out = new FileOutputStream(to);
            int readCount;
            byte[] bytes = new byte[1024];
            while ((readCount = in.read(bytes)) != -1) {
                out.write(bytes, 0, readCount);
            }
            out.flush();
            in.close();
            out.close();
        } catch (Exception ex) {
            throw new BuildException(ex.getMessage(), ex);
        }
    }
