    private File prepareArchive() throws IOException {
        if (debug) System.out.println("Preparing archive...");
        List additionalList = version.getAdditionalContentList();
        for (Iterator i = additionalList.iterator(); i.hasNext(); ) {
            AdditionalContent ac = (AdditionalContent) i.next();
            addFiles(ac.getContentDir(), ac.getContentPath());
        }
        File archive = File.createTempFile("content-archive", ".zip");
        try {
            ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(archive));
            outputStream.setMethod(ZipOutputStream.DEFLATED);
            outputStream.setLevel(9);
            byte buffer[] = new byte[4096];
            for (Iterator i = archiveFiles.iterator(); i.hasNext(); ) {
                ArchiveFile af = (ArchiveFile) i.next();
                ZipEntry entry = new ZipEntry(af.getPath());
                outputStream.putNextEntry(entry);
                FileInputStream inputStream = new FileInputStream(af.getFile());
                while (true) {
                    int len = inputStream.read(buffer);
                    if (len == -1) break;
                    outputStream.write(buffer, 0, len);
                }
                inputStream.close();
                outputStream.closeEntry();
            }
            outputStream.finish();
            outputStream.close();
        } catch (IOException e) {
            archive.delete();
            throw e;
        }
        return archive;
    }
