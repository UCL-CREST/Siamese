    public StringBuffer getCompoundZip(DmsDocument document, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
        SessionContainer sessionContainer = this.getSessionContainer(request);
        Connection conn = this.getConnection(request);
        DocumentRetrievalManager docRetrievalManager = new DocumentRetrievalManager(sessionContainer, conn);
        RootRetrievalManager rootRetrievalManager = new RootRetrievalManager(sessionContainer, conn);
        DmsContentManager dmsContentManager = new DmsContentManager(sessionContainer, conn);
        InputStream inputStream = null;
        InputStream dataStream = null;
        String zipfilename = document.getDocumentName() + ".zip";
        StringBuffer comZipFile = new StringBuffer();
        DmsLocMaster locMaster = rootRetrievalManager.getTargetLocMasterByDocument(document);
        String locMasterPath = locMaster.getLocPath();
        comZipFile = comZipFile.append(locMasterPath).append("/").append(zipfilename);
        try {
            ZipOutputStream zipOutStream = new ZipOutputStream(new FileOutputStream(comZipFile.toString()));
            DocumentRetrievalManager docRetrieval = new DocumentRetrievalManager(sessionContainer, conn);
            zipOutStream.setEncoding(docRetrieval.getZipFileDefaultEncode());
            DocumentOperationManager docOperationManager = new DocumentOperationManager(sessionContainer, conn);
            MtmDocumentRelationshipDAObject docRelationshipDAO = new MtmDocumentRelationshipDAObject(sessionContainer, conn);
            Integer targetID = document.getID();
            Integer contentID = null;
            List list = docOperationManager.getSubDocumentByParentID(document.getID());
            if (DmsDocument.COMPOUND_DOC_TYPE.equals(document.getDocumentType())) {
                list.add(document);
            }
            if (!Utility.isEmpty(list)) {
                for (int j = 0; j < list.size(); j++) {
                    DmsDocument dmsDocument = (DmsDocument) list.get(j);
                    String fName = dmsDocument.getDocumentName();
                    DmsVersion sVersion = docRetrievalManager.getTopVersionByDocumentID(dmsDocument.getID());
                    contentID = sVersion.getContentID();
                    if (DmsDocument.DOCUMENT_LINK.equals(dmsDocument.getDocumentType())) {
                        Integer targetDocID = docRelationshipDAO.getTargetDocIDByRelatedDocID(dmsDocument.getID(), dmsDocument.getDocumentType());
                        DmsDocument targetDoc = docRetrievalManager.getDocument(targetDocID);
                        if (targetDoc != null) {
                            targetDoc.setDocumentName(dmsDocument.getDocumentName());
                            targetDoc.setDocumentType(dmsDocument.getDocumentType());
                            targetID = targetDocID;
                            dmsDocument = targetDoc;
                            sVersion = docRetrievalManager.getTopVersionByDocumentID(targetID);
                            contentID = sVersion.getContentID();
                        } else {
                            document.setRecordStatus(GlobalConstant.RECORD_STATUS_INACTIVE);
                        }
                    }
                    if (!GlobalConstant.RECORD_STATUS_INACTIVE.equals(document.getRecordStatus())) {
                        DmsContent docContent = docRetrievalManager.getContentByContentID(contentID);
                        dataStream = dmsContentManager.readDmsDocumentStoreContent(dmsDocument, docContent);
                        ZipEntry theEntry = new ZipEntry(fName);
                        zipOutStream.putNextEntry(theEntry);
                        byte[] buffer = new byte[8192];
                        int length = -1;
                        inputStream = dataStream;
                        while ((length = inputStream.read(buffer, 0, 8192)) != -1) {
                            zipOutStream.write(buffer, 0, length);
                        }
                    }
                }
            }
            zipOutStream.flush();
            zipOutStream.close();
        } catch (Exception e) {
            log.error(e, e);
            throw new ApplicationException(com.dcivision.framework.ErrorConstant.COMMON_FATAL_ERROR, e);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ignore) {
                inputStream = null;
            }
        }
        return comZipFile;
    }
