    public static boolean copy(File from, File to) {
        if (from.isDirectory()) {
            String[] contents = from.list();
            for (int i = 0; contents != null && i < contents.length; i++) {
                copy(from, to, contents[i]);
            }
        } else {
            try {
                OutputStream os = makeFile(to);
                InputStream is = new FileInputStream(from);
                pipe(is, os, false);
                is.close();
                os.close();
            } catch (IOException ex) {
                return false;
            }
        }
        long time = from.lastModified();
        if (!to.setLastModified(time)) {
            return false;
        }
        long newtime = to.lastModified();
        return time == newtime;
    }
