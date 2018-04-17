    static void addFileToZip(ZipOutputStream zipout, File file) throws FileNotFoundException, IOException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        FileInputStream filein = null;
        try {
            filein = new FileInputStream(file);
            int s;
            while ((s = filein.read()) != -1) {
                bout.write(s);
            }
        } finally {
            filein.close();
        }
        zipout.putNextEntry(new ZipEntry(file.getName()));
        zipout.write(bout.toByteArray());
        zipout.flush();
    }
