    private static void copyFile(File source, File dest, boolean visibleFilesOnly) throws IOException {
        if (visibleFilesOnly && isHiddenOrDotFile(source)) {
            return;
        }
        if (dest.exists()) {
            System.err.println("Destination File Already Exists: " + dest);
        }
        FileChannel in = null, out = null;
        try {
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(dest).getChannel();
            in.transferTo(0, in.size(), out);
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }
