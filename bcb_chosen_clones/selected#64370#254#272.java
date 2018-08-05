    public void createArchive() throws LTSException {
        if (getFile().exists()) return;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            fos = new FileOutputStream(getFile());
            zos = new ZipOutputStream(fos);
            ZipEntry zentry = new ZipEntry("junk");
            zos.putNextEntry(zentry);
            String s = "A zip file requires at least one entry.";
            zos.write(s.getBytes());
            zos.close();
        } catch (IOException e) {
            throw new LTSException("Error creating archive file, " + getFile(), e);
        } finally {
            IOUtilities.closeNoExceptions(fos);
            IOUtilities.closeNoExceptions(zos);
        }
    }
