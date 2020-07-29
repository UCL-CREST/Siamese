    public void copyDirectory(final File sourceLocation, final File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }
            final String[] children = sourceLocation.list();
            for (String aChildren : children) {
                copyDirectory(new File(sourceLocation, aChildren), new File(targetLocation, aChildren));
            }
        } else {
            final InputStream in = new FileInputStream(sourceLocation);
            final OutputStream out = new FileOutputStream(targetLocation);
            final byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }
