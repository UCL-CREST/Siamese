    public static void zip(Map<String, InputStream> map, OutputStream os, int level, String comment) throws IOException {
        ZipOutputStream out = new ZipOutputStream(os);
        if (level != -1) out.setLevel(level);
        if (comment != null) out.setComment(comment);
        byte[] buf = new byte[4096];
        for (Map.Entry<String, InputStream> entry : map.entrySet()) {
            BufferedInputStream in = null;
            try {
                in = new BufferedInputStream(entry.getValue());
                ZipEntry zipEntry = new ZipEntry(entry.getKey());
                out.putNextEntry(zipEntry);
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
            } finally {
                if (in != null) in.close();
            }
        }
        out.finish();
    }
