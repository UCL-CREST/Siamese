    protected void copyFile(File source, File destination) throws ApplicationException {
        try {
            OutputStream out = new FileOutputStream(destination);
            DataInputStream in = new DataInputStream(new FileInputStream(source));
            byte[] buf = new byte[8192];
            for (int nread = in.read(buf); nread > 0; nread = in.read(buf)) {
                out.write(buf, 0, nread);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            throw new ApplicationException("Can't copy file " + source + " to " + destination);
        }
    }
