    public ActionForward downloadDS(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'datastore download' method");
        }
        DocServiceManager mgr = (DocServiceManager) getBean("docServiceManager");
        List<Document> docsInDS = mgr.listDocuments();
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + "safe-ds.zip\"");
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        try {
            CRC32 crc = new CRC32();
            for (Document doc : docsInDS) {
                String fileName = doc.getDocumentID();
                if (!fileName.endsWith(".xml")) {
                    fileName += ".xml";
                }
                byte[] docData = mgr.getDocXML(doc.getDocumentID());
                ZipEntry entry = new ZipEntry(fileName);
                entry.setSize(docData.length);
                crc.reset();
                crc.update(docData);
                entry.setCrc(crc.getValue());
                zipOut.putNextEntry(entry);
                try {
                    zipOut.write(docData);
                } finally {
                    zipOut.closeEntry();
                }
            }
        } finally {
            zipOut.close();
        }
        return null;
    }
