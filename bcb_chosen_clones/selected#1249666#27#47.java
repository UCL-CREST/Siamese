    @Override
    protected void setUp() {
        try {
            InputStream is = Support_Resources.getStream("hyts_ZipFile.zip");
            if (is == null) {
                System.out.println("file hyts_ZipFile.zip can not be found");
            }
            zis = new ZipInputStream(is);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(bos);
            ZipEntry entry = new ZipEntry("myFile");
            zos.putNextEntry(entry);
            zos.write(dataBytes);
            zos.closeEntry();
            zos.close();
            zipBytes = bos.toByteArray();
        } catch (Exception e) {
            System.out.println("Exception during ZipFile setup:");
            e.printStackTrace();
        }
    }
