    public void deleteEntries(String[] entries) {
        ZipOutputStream zout = null;
        ZipInputStream zin = null;
        File tmpzip = null;
        try {
            tmpzip = File.createTempFile("zip", ".tmp", new File("."));
            zin = new ZipInputStream(new FileInputStream(selfFile));
            zout = new ZipOutputStream(new FileOutputStream(tmpzip));
            ZipEntry ze;
            int len = 0;
            byte[] b = new byte[4096];
            while ((ze = zin.getNextEntry()) != null) {
                if (getContainEntry(ze.getName(), entries) != -1) {
                    zin.closeEntry();
                    count--;
                    continue;
                }
                zout.putNextEntry(new ZipEntry(ze.getName()));
                while ((len = zin.read(b)) != -1) {
                    zout.write(b, 0, len);
                }
                zout.closeEntry();
                zin.closeEntry();
            }
            zout.close();
            zin.close();
            String slefFileName = selfFile.getPath();
            selfFile.delete();
            tmpzip.renameTo(new File(slefFileName));
            selfFile = new File(slefFileName);
            isChanged = true;
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        }
    }
