    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(RESULT2));
        document.open();
        List<Element> objects = HTMLWorker.parseToList(new FileReader(HTML), null, providers);
        for (Element element : objects) {
            document.add(element);
        }
        document.close();
    }
