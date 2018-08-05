    public void run() {
        try {
            File file = source.getFile(path);
            ZipEntry entry = new ZipEntry(path.substring(1));
            entry.setTime(source.getLastModified(path).getTime());
            entry.setComment(file.getCreator());
            zip.putNextEntry(entry);
            file.write(zip);
            zip.closeEntry();
            if (metadata) {
                byte[] metabytes = new String(DocumentFactory.getRDFChars(file.getMetaData())).getBytes("UTF-8");
                entry = new ZipEntry(path.substring(1) + ".rdf");
                entry.setTime(source.getLastModified(path).getTime());
                zip.putNextEntry(entry);
                zip.write(metabytes);
                zip.closeEntry();
            }
            success();
        } catch (Exception e) {
            fatalError(e, null);
        }
    }
