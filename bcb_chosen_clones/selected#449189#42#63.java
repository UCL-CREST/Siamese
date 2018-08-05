    public File createArchive(String filename) throws ArchiveFailedException {
        File f = new File(filename);
        try {
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(f));
            zout.putNextEntry(new ZipEntry(COURSE_DESCRIPTOR));
            zout.write(getXML().getBytes());
            zout.closeEntry();
            for (ExportedFile ef : getFiles()) {
                zout.putNextEntry(new ZipEntry(ef.getFilename()));
                zout.write(ef.getContent());
                zout.closeEntry();
            }
            zout.close();
        } catch (Exception e) {
            throw new ArchiveFailedException(e);
        }
        return f;
    }
