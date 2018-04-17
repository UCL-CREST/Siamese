    public ActionForward downloadCorpus(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Entering 'corpus download' method");
        }
        DocServiceManager mgr = (DocServiceManager) getBean("docServiceManager");
        String corpusID = request.getParameter("corpusID");
        String corpusName = mgr.getCorpusName(corpusID);
        List<Document> docsInCorpus = mgr.listDocuments(corpusID);
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + corpusName + ".zip\"");
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        try {
            CRC32 crc = new CRC32();
            for (Document doc : docsInCorpus) {
                String fileName = doc.getDocumentName();
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
