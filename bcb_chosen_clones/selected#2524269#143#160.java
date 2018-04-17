    private void writeDiveSiteDocuments(ZipOutputStream zos, List<DiveSite> diveSites, DiveSiteManagerFacade diveSiteManagerFacade) throws IOException {
        String base = "documents" + File.separatorChar + "divesites";
        for (DiveSite diveSite : diveSites) {
            if (diveSite.getDocuments() != null) {
                ZipEntry ze = null;
                for (be.vds.jtbdive.core.core.Document doc : diveSite.getDocuments()) {
                    try {
                        ze = new ZipEntry(base + File.separatorChar + doc.getId() + "." + doc.getDocumentFormat().getExtension());
                        zos.putNextEntry(ze);
                        zos.write(diveSiteManagerFacade.loadDocumentContent(doc.getId(), doc.getDocumentFormat()));
                        zos.flush();
                    } catch (DataStoreException e) {
                        LOGGER.error(e);
                    }
                }
            }
        }
    }
