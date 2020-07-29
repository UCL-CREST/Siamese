    private void generateDocFile(String srcFileName, String s, String destFileName) {
        try {
            ZipFile docxFile = new ZipFile(new File(srcFileName));
            ZipEntry documentXML = docxFile.getEntry("word/document.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            InputStream documentXMLIS1 = docxFile.getInputStream(documentXML);
            Document doc = dbf.newDocumentBuilder().parse(documentXMLIS1);
            Transformer t = TransformerFactory.newInstance().newTransformer();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            t.transform(new DOMSource(doc), new StreamResult(baos));
            ZipOutputStream docxOutFile = new ZipOutputStream(new FileOutputStream(destFileName));
            Enumeration<ZipEntry> entriesIter = (Enumeration<ZipEntry>) docxFile.entries();
            while (entriesIter.hasMoreElements()) {
                ZipEntry entry = entriesIter.nextElement();
                if (entry.getName().equals("word/document.xml")) {
                    docxOutFile.putNextEntry(new ZipEntry(entry.getName()));
                    byte[] datas = s.getBytes("UTF-8");
                    docxOutFile.write(datas, 0, datas.length);
                    docxOutFile.closeEntry();
                } else if (entry.getName().equals("word/media/image1.png")) {
                    InputStream incoming = new FileInputStream("c:/aaa.jpg");
                    byte[] data = new byte[incoming.available()];
                    int readCount = incoming.read(data, 0, data.length);
                    docxOutFile.putNextEntry(new ZipEntry(entry.getName()));
                    docxOutFile.write(data, 0, readCount);
                    docxOutFile.closeEntry();
                } else {
                    InputStream incoming = docxFile.getInputStream(entry);
                    byte[] data = new byte[incoming.available()];
                    int readCount = incoming.read(data, 0, data.length);
                    docxOutFile.putNextEntry(new ZipEntry(entry.getName()));
                    docxOutFile.write(data, 0, readCount);
                    docxOutFile.closeEntry();
                }
            }
            docxOutFile.close();
        } catch (Exception e) {
        }
    }
