    protected static void copyOrMove(File sourceLocation, File targetLocation, boolean move) throws IOException {
        String[] children;
        int i;
        InputStream in;
        OutputStream out;
        byte[] buf;
        int len;
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) targetLocation.mkdir();
            children = sourceLocation.list();
            for (i = 0; i < children.length; i++) {
                copyOrMove(new File(sourceLocation, children[i]), new File(targetLocation, children[i]), move);
            }
            if (move) sourceLocation.delete();
        } else {
            in = new FileInputStream(sourceLocation);
            if (targetLocation.isDirectory()) out = new FileOutputStream(targetLocation.getAbsolutePath() + File.separator + sourceLocation.getName()); else out = new FileOutputStream(targetLocation);
            buf = new byte[1024];
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            in.close();
            out.close();
            if (move) sourceLocation.delete();
        }
    }
