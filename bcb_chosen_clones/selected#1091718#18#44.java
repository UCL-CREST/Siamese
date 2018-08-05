    public static void copyCompletely(InputStream input, OutputStream output) throws IOException {
        if ((output instanceof FileOutputStream) && (input instanceof FileInputStream)) {
            try {
                FileChannel target = ((FileOutputStream) output).getChannel();
                FileChannel source = ((FileInputStream) input).getChannel();
                source.transferTo(0, Integer.MAX_VALUE, target);
                source.close();
                target.close();
                return;
            } catch (Exception e) {
            }
        }
        byte[] buf = new byte[8192];
        while (true) {
            int length = input.read(buf);
            if (length < 0) break;
            output.write(buf, 0, length);
        }
        try {
            input.close();
        } catch (IOException ignore) {
        }
        try {
            output.close();
        } catch (IOException ignore) {
        }
    }
