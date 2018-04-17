    @Override
    public void copy(File source, File dest) throws IOException {
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = (new FileInputStream(source)).getChannel();
            out = (new FileOutputStream(dest)).getChannel();
            in.transferTo(0, source.length(), out);
        } catch (FileNotFoundException e) {
            throw new IOException("Wrong source or destination path for backup operation!");
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }
