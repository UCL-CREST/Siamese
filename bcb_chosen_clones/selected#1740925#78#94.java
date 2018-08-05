    public void test_putNextEntryLjava_util_zip_ZipEntry() throws IOException {
        ZipEntry ze = new ZipEntry("testEntry");
        ze.setTime(System.currentTimeMillis());
        zos.putNextEntry(ze);
        zos.write("Hello World".getBytes());
        zos.closeEntry();
        zos.close();
        zis = new ZipInputStream(new ByteArrayInputStream(bos.toByteArray()));
        ZipEntry ze2 = zis.getNextEntry();
        zis.closeEntry();
        assertTrue("Failed to write correct entry", ze.getName().equals(ze2.getName()) && ze.getCrc() == ze2.getCrc());
        try {
            zos.putNextEntry(ze);
            fail("Entry with incorrect setting failed to throw exception");
        } catch (IOException e) {
        }
    }
