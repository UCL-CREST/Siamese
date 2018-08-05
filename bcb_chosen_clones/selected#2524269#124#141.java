    private void writeDiveDocuments(ZipOutputStream zos, List<Dive> dives, LogBookManagerFacade logBookManagerFacade) throws IOException {
        String base = "documents" + File.separatorChar + "dives";
        for (Dive dive : dives) {
            if (dive.getDocuments() != null) {
                ZipEntry ze = null;
                for (be.vds.jtbdive.core.core.Document doc : dive.getDocuments()) {
                    try {
                        ze = new ZipEntry(base + File.separatorChar + doc.getId() + "." + doc.getDocumentFormat().getExtension());
                        zos.putNextEntry(ze);
                        zos.write(logBookManagerFacade.loadDocumentContent(doc.getId(), doc.getDocumentFormat()));
                        zos.flush();
                    } catch (DataStoreException e) {
                        LOGGER.error(e);
                    }
                }
            }
        }
    }
