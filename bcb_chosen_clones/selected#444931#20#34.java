    public static void main(String[] args) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        writer.setInitialLeading(18);
        document.open();
        Image button = Image.getInstance(RESOURCE);
        document.add(getTitleBar(writer, button, "My first title"));
        for (int i = 0; i < 20; i++) document.add(new Paragraph("Some text: " + i));
        document.add(getTitleBar(writer, button, "My second title"));
        for (int i = 0; i < 10; i++) document.add(new Paragraph("Some text: " + i));
        document.newPage();
        document.add(getTitleBar(writer, button, "My third title"));
        for (int i = 0; i < 10; i++) document.add(new Paragraph("Some text: " + i));
        document.close();
    }
