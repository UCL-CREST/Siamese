    static boolean copyFiles(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.equals(targetLocation)) return false;
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyFiles(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
            }
        } else if (sourceLocation.exists()) {
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
        return true;
    }
