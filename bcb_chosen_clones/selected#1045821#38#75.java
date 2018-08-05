    public void copyFile(File a_fileSrc, File a_fileDest, boolean a_append) throws IOException {
        a_fileDest.getParentFile().mkdirs();
        FileInputStream in = null;
        FileOutputStream out = null;
        FileChannel fcin = null;
        FileChannel fcout = null;
        try {
            in = new FileInputStream(a_fileSrc);
            out = new FileOutputStream(a_fileDest, a_append);
            fcin = in.getChannel();
            fcout = out.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(16 * 1024);
            while (true) {
                buffer.clear();
                int r = fcin.read(buffer);
                if (r == -1) {
                    break;
                }
                buffer.flip();
                fcout.write(buffer);
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (fcin != null) {
                fcin.close();
            }
            if (fcout != null) {
                fcout.close();
            }
        }
    }
