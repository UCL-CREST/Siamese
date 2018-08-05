    public static void CopyDirectory(File sourceLocation, File targetLocation, boolean ignore_dot_files) throws IOException {
        if (ignore_dot_files) {
            if (sourceLocation.getName().startsWith(".")) {
                return;
            }
        }
        System.out.println("Copying " + sourceLocation);
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdirs();
            }
            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                CopyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]), ignore_dot_files);
            }
        } else {
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
    }
