    private void addFilesToSelf(File path, String[] fileNames, boolean overWrite) {
        BufferedInputStream bin;
        ZipOutputStream zout;
        ZipInputStream zin;
        File tmpzip = null;
        ZipEntry[] addedEntries = new ZipEntry[fileNames.length];
        for (int i = 0; i < fileNames.length; i++) {
            addedEntries[i] = new ZipEntry(fileNames[i]);
        }
        try {
            tmpzip = File.createTempFile("jzj", ".tmp", workDirectory);
            zin = new ZipInputStream(new FileInputStream(selfFile));
            zout = new ZipOutputStream(new FileOutputStream(tmpzip));
            ZipEntry entry;
            int len = 0;
            byte[] b = new byte[4096];
            while ((entry = zin.getNextEntry()) != null) {
                if (!isContainEntry(entry, addedEntries)) {
                    zout.putNextEntry(new ZipEntry(entry.getName()));
                    while ((len = zin.read(b)) != -1) {
                        zout.write(b, 0, len);
                    }
                    zout.closeEntry();
                    zin.closeEntry();
                } else if (!overWrite) {
                    zout.close();
                    zin.close();
                    tmpzip.delete();
                    tmpzip = null;
                    throw new ZipException(ZipException.ENTRYEXIST);
                }
            }
            for (int i = 0; i < addedEntries.length; i++) {
                bin = new BufferedInputStream(new FileInputStream(new File(path, fileNames[i])));
                zout.putNextEntry(addedEntries[i]);
                while ((len = bin.read(b)) != -1) {
                    zout.write(b, 0, len);
                }
                zout.closeEntry();
                count++;
            }
            zout.close();
            zin.close();
            String slefFileName = selfFile.getPath();
            logger.debug("rename to:" + slefFileName);
            selfFile.delete();
            tmpzip.renameTo(new File(slefFileName));
            selfFile = new File(slefFileName);
            isChanged = true;
        } catch (Exception e) {
            if (tmpzip != null) {
                tmpzip.delete();
            }
            e.printStackTrace();
        }
    }
