    private void addToZip(File fileToZip, ZipOutputStream out, String extractPath) throws IOException {
        if (fileToZip.isDirectory()) {
            File[] files = fileToZip.listFiles();
            for (File toAdd : files) {
                addToZip(toAdd.getAbsoluteFile(), out, extractPath);
            }
        } else {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(fileToZip));
            String newPath = fileToZip.getAbsolutePath().substring(extractPath.length() + 1);
            out.putNextEntry(new ZipEntry(newPath));
            int len;
            byte[] buf = new byte[BUFFER_SIZE];
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
    }
