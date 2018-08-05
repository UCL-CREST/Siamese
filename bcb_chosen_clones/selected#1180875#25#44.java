    public static void copy(File _from, File _to) throws IOException {
        if (_from == null || !_from.exists()) return;
        FileOutputStream out = null;
        FileInputStream in = null;
        try {
            out = new FileOutputStream(_to);
            in = new FileInputStream(_from);
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
