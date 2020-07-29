    public static void copy(File srcPath, File dstPath) throws IOException {
        if (srcPath.isDirectory()) {
            if (!dstPath.exists()) {
                boolean result = dstPath.mkdir();
                if (!result) throw new IOException("Unable to create directoy: " + dstPath);
            }
            String[] files = srcPath.list();
            for (String file : files) {
                copy(new File(srcPath, file), new File(dstPath, file));
            }
        } else {
            if (srcPath.exists()) {
                FileChannel in = null;
                FileChannel out = null;
                try {
                    in = new FileInputStream(srcPath).getChannel();
                    out = new FileOutputStream(dstPath).getChannel();
                    long size = in.size();
                    MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY, 0, size);
                    out.write(buf);
                } finally {
                    if (in != null) in.close();
                    if (out != null) out.close();
                }
            }
        }
    }
