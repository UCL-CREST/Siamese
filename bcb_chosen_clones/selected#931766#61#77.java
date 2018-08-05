    private byte[] createPdfTemplateData(int pageNum) throws Exception {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();
        for (int i = 0; i < pageNum; i++) {
            document.add(new Phrase("page:" + (i + 1)));
            TextField tf = new TextField(writer, new Rectangle(100, 400, 200, 500), "name" + i);
            tf.setText("value=asdf");
            writer.addAnnotation(tf.getTextField());
            document.newPage();
        }
        document.close();
        writer.close();
        byte[] bs = baos.toByteArray();
        return bs;
    }
