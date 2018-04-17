    private static byte[] getByteArrayFromFile(final File file) throws FileNotFoundException, IOException {
        final FileInputStream fis = new FileInputStream(file);
        try {
            final ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
            final byte[] buffer = new byte[1024];
            int cnt;
            while ((cnt = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, cnt);
            }
            return bos.toByteArray();
        } finally {
            fis.close();
        }
    }
