    public static final void zip(String fileName, final String zipName) throws Exception {
        try {
            final File file = new File(fileName);
            final FileInputStream fileInputStream = new FileInputStream(file);
            final ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipName));
            if (fileName.indexOf(File.separator) != -1) fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1);
            zipOutputStream.putNextEntry(new ZipEntry(fileName));
            final byte[] buf = new byte[1024];
            int len;
            while ((len = fileInputStream.read(buf)) > 0) {
                zipOutputStream.write(buf, 0, len);
            }
            zipOutputStream.closeEntry();
            fileInputStream.close();
            zipOutputStream.close();
        } catch (final IOException e) {
            System.out.println("zip: Error: " + e.getMessage());
            throw (e);
        }
    }
