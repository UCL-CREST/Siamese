    protected void storeDocuments() throws DocumentStoreException {
        ReportFileHome reportFileHome = null;
        try {
            Context initialContext = new InitialContext();
            reportFileHome = (ReportFileHome) PortableRemoteObject.narrow(initialContext.lookup(ReportFileHome.COMP_NAME), ReportFileHome.class);
        } catch (Exception e) {
            throw new DocumentStoreException("ReportFileHome could not be loaded");
        }
        int count = 0;
        String filename;
        String title, ending;
        byte[] b;
        ReportFileObject reportFileObject;
        Enumeration enumTitles;
        try {
            enumTitles = reportDocHandler.getReportDocumentTitles().elements();
        } catch (RemoteException re) {
            enumTitles = new Vector().elements();
        }
        while (enumTitles.hasMoreElements()) {
            try {
                filename = (String) enumTitles.nextElement();
                ending = filename.substring(filename.lastIndexOf('.') + 1);
                title = filename.substring(0, filename.lastIndexOf('.'));
                b = reportDocHandler.getReportDocumentStream(filename);
                if (filename.endsWith("HTML") | filename.endsWith("html")) {
                    zipStream = new ByteArrayOutputStream();
                    ZipOutputStream zip = new ZipOutputStream(zipStream);
                    ZipEntry entry = new ZipEntry(filename);
                    zip.putNextEntry(entry);
                    zip.write(b);
                    zip.closeEntry();
                    String key;
                    Set imageSet = imageByteArrays.keySet();
                    Iterator iterCharts = imageSet.iterator();
                    while (iterCharts.hasNext()) {
                        key = (String) iterCharts.next();
                        b = (byte[]) imageByteArrays.get(key);
                        entry = new ZipEntry("images/" + key);
                        zip.putNextEntry(entry);
                        zip.write(b);
                        zip.closeEntry();
                    }
                    zip.close();
                    UniqueKeyGenerator primkeygen = new UniqueKeyGenerator();
                    reportFileObject = reportFileHome.create(primkeygen.getUniqueId(), title, zipStream.toByteArray(), new Date(), reportID, "ZIP");
                    reportfiles.addElement(reportFileObject);
                    zipStream = null;
                    count++;
                } else {
                    UniqueKeyGenerator primkeygen = new UniqueKeyGenerator();
                    reportFileObject = reportFileHome.create(primkeygen.getUniqueId(), title, b, new Date(), reportID, ending);
                    reportfiles.addElement(reportFileObject);
                    count++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            reportDocHandler.deleteDocuments();
        } catch (RemoteException re) {
        }
        try {
            enumTitles = dataDocHandler.getDataDocumentTitles().elements();
        } catch (RemoteException re) {
            enumTitles = new Vector().elements();
        }
        while (enumTitles.hasMoreElements()) {
            try {
                filename = (String) enumTitles.nextElement();
                ending = filename.substring(filename.lastIndexOf('.') + 1);
                title = filename.substring(0, filename.lastIndexOf('.'));
                b = dataDocHandler.getDataDocumentStream(filename);
                UniqueKeyGenerator primkeygen = new UniqueKeyGenerator();
                reportFileObject = reportFileHome.create(primkeygen.getUniqueId(), title, b, new Date(), reportID, ending);
                reportfiles.addElement(reportFileObject);
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            dataDocHandler.deleteDocuments();
        } catch (RemoteException re) {
        }
        if (count == 0) throw new DocumentStoreException("No document could be stored.");
    }
