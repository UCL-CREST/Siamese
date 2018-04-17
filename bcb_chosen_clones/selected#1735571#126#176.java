    private void addFileToSelf(File path, String fileName, boolean overWrite) {
        BufferedInputStream bin;
        ZipOutputStream zout;
        ZipInputStream zin;
        File tmpzip = null;
        ZipEntry addedEntry = new ZipEntry(fileName);
        try {
            tmpzip = File.createTempFile("jzj", ".tmp", workDirectory);
            zin = new ZipInputStream(new FileInputStream(selfFile));
            zout = new ZipOutputStream(new FileOutputStream(tmpzip));
            ZipEntry entry;
            int len;
            byte[] b = new byte[4096];
            while ((entry = zin.getNextEntry()) != null) {
                if (!isSameEntry(entry, addedEntry)) {
                    zout.putNextEntry(new ZipEntry(entry.getName()));
                    while ((len = zin.read(b)) != -1) {
                        zout.write(b, 0, len);
                    }
                    zout.closeEntry();
                    zin.closeEntry();
                } else if (overWrite) {
                } else {
                    zout.close();
                    zin.close();
                    tmpzip.delete();
                    tmpzip = null;
                    throw new ZipException(ZipException.ENTRYEXIST);
                }
            }
            bin = new BufferedInputStream(new FileInputStream(new File(path, fileName)));
            zout.putNextEntry(addedEntry);
            while ((len = bin.read(b)) != -1) {
                zout.write(b, 0, len);
            }
            zout.closeEntry();
            zout.close();
            zin.close();
            String selfFileName = selfFile.getPath();
            selfFile.delete();
            tmpzip.renameTo(new File(selfFileName));
            selfFile = new File(selfFileName);
            isChanged = true;
            count++;
        } catch (Exception e) {
            if (tmpzip != null) {
                tmpzip.delete();
            }
            e.printStackTrace();
        }
    }
