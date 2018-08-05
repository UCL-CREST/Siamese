    private synchronized boolean delFile(String entryName) {
        if (entryName.trim().length() == 0) return false;
        int buffer = 2048;
        byte[] data = new byte[buffer];
        FileInputStream fis;
        FileOutputStream fos;
        BufferedInputStream source;
        int count;
        try {
            File tempResFile = new File(resFileOnDisk.getCanonicalPath() + ".bak");
            if (!tempResFile.exists()) tempResFile.createNewFile();
            fis = new FileInputStream(resFileOnDisk);
            fos = new FileOutputStream(tempResFile);
            source = new BufferedInputStream(fis, buffer);
            while ((count = source.read(data, 0, buffer)) != -1) {
                fos.write(data, 0, count);
            }
            fos.close();
            source.close();
            fis.close();
            ZipFile oldZip = new ZipFile(tempResFile);
            resFile = null;
            fos = new FileOutputStream(resFileOnDisk);
            ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(fos));
            Enumeration e = oldZip.entries();
            while (e.hasMoreElements()) {
                ZipEntry tmpEntry = (ZipEntry) e.nextElement();
                if (!tmpEntry.getName().equals(entryName)) {
                    ZipEntry newEntry = new ZipEntry(tmpEntry.getName());
                    newEntry.setComment(tmpEntry.getComment());
                    zos.putNextEntry(newEntry);
                    source = new BufferedInputStream(oldZip.getInputStream(tmpEntry), buffer);
                    while ((count = source.read(data, 0, buffer)) != -1) {
                        zos.write(data, 0, count);
                    }
                    source.close();
                }
            }
            zos.close();
            fos.close();
            resFile = new ZipFile(resFileOnDisk);
            oldZip = null;
            tempResFile.deleteOnExit();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return true;
    }
