    private void writeEntry(File file, ZipOutputStream output, File oya) throws IOException {
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
        String fn = extractRelativeZipPath(file.getAbsolutePath(), oya.getAbsolutePath());
        ZipEntry entry = new ZipEntry(this.convertZipEntrySeparator(fn));
        output.putNextEntry(entry);
        int b;
        while ((b = input.read()) != -1) {
            output.write(b);
        }
        input.close();
        output.closeEntry();
    }
