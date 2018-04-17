    private void copy(File fin, File fout) throws IOException {
        FileOutputStream out = null;
        FileInputStream in = null;
        try {
            out = new FileOutputStream(fout);
            in = new FileInputStream(fin);
            byte[] buf = new byte[2048];
            int read = in.read(buf);
            while (read > 0) {
                out.write(buf, 0, read);
                read = in.read(buf);
            }
        } catch (IOException _e) {
            throw _e;
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }
