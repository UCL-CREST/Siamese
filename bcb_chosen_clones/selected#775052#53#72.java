    public static void copy(File srcDir, File dstDir) throws IOException {
        if (srcDir.isDirectory()) {
            if (!dstDir.exists()) dstDir.mkdir();
            String[] children = srcDir.list();
            for (int i = 0; i < children.length; i++) copy(new File(srcDir, children[i]), new File(dstDir, children[i]));
        } else {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = new FileInputStream(srcDir);
                out = new FileOutputStream(dstDir);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            } finally {
                Util.close(in);
                Util.close(out);
            }
        }
    }
