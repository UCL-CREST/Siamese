    private static void writeFile(File file, ZipOutputStream out) throws IOException {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            out.putNextEntry(new ZipEntry(file.getName()));
            byte[] buffer = new byte[BUFFER_SIZE];
            int read = -1;
            do {
                read = in.read(buffer);
                if (read > -1) {
                    out.write(buffer, 0, read);
                }
            } while (read > -1);
            out.closeEntry();
        } catch (IOException e) {
            throw e;
        } finally {
            if (in != null) in.close();
        }
    }
