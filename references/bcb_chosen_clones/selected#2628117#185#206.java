    public static void zip(InputStream is, File toFile, boolean closeInputStream) throws IOException {
        final int BUFFER_SIZE = 2048;
        BufferedInputStream in = new BufferedInputStream(is, BUFFER_SIZE);
        try {
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(toFile)));
            try {
                ZipEntry entry = new ZipEntry(toFile.getName());
                out.putNextEntry(entry);
                byte data[] = new byte[BUFFER_SIZE];
                int count;
                while ((count = in.read(data, 0, BUFFER_SIZE)) != -1) {
                    out.write(data, 0, count);
                }
            } finally {
                out.close();
            }
        } finally {
            if (closeInputStream) {
                in.close();
            }
        }
    }
