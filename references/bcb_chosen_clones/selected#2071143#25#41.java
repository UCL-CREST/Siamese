    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        Image img = Image.getInstance(RESOURCE);
        float w = img.getScaledWidth();
        float h = img.getScaledHeight();
        PdfTemplate t = writer.getDirectContent().createTemplate(850, 600);
        t.ellipse(0, 0, 850, 600);
        t.clip();
        t.newPath();
        t.addImage(img, w, 0, 0, h, 0, -600);
        Image clipped = Image.getInstance(t);
        clipped.scalePercent(50);
        document.add(clipped);
        document.close();
    }
