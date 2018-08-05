    public void write(T aDocument, OutputStream aOutputStream) throws IOException {
        LOGGER.info("Writing document to stream");
        ZipOutputStream theZipOut = new ZipOutputStream(aOutputStream);
        for (GenericDocumentEntry theEntry : aDocument.getEntries()) {
            LOGGER.info("Writing entry " + theEntry.getName());
            ZipEntry theZipEntry = new ZipEntry(theEntry.getName());
            theZipOut.putNextEntry(theZipEntry);
            theZipOut.write(theEntry.getData());
            theZipOut.closeEntry();
        }
        theZipOut.close();
        LOGGER.info("Finished");
    }
