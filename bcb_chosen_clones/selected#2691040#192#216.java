    @Test
    public void ZipFiles() {
        SampleFileWriter.createFile("myfile.ump", "findme");
        String[] filesToZip = new String[1];
        filesToZip[0] = "myfile.ump";
        byte[] buffer = new byte[18024];
        String zipFileName = "myfile.zip";
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
            out.setLevel(Deflater.DEFAULT_COMPRESSION);
            for (int i = 0; i < filesToZip.length; i++) {
                FileInputStream in = new FileInputStream(filesToZip[i]);
                out.putNextEntry(new ZipEntry(filesToZip[i]));
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (Exception e) {
            throw new RuntimeException("Unable to create zip file", e);
        }
    }
