    protected void addFile(File newEntry, String name) {
        if (newEntry.isDirectory()) {
            return;
        }
        try {
            ZipEntry ze = new ZipEntry(name);
            mZos.putNextEntry(ze);
            FileInputStream fis = new FileInputStream(newEntry);
            byte fdata[] = new byte[512];
            int readCount = 0;
            while ((readCount = fis.read(fdata)) != -1) {
                mZos.write(fdata, 0, readCount);
            }
            fis.close();
            mZos.closeEntry();
            mObserverCont.setNext(ze);
            mObserverCont.setCount(++miCurrentCount);
        } catch (Exception ex) {
            mObserverCont.setError(ex.getMessage());
        }
    }
