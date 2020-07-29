    public static void main(String[] args) throws IOException, DocumentException {
        Document document = new Document(PageSize.POSTCARD, 30, 30, 30, 30);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        writer.setCompressionLevel(0);
        document.open();
        Image img = Image.getInstance(RESOURCE);
        img.setAbsolutePosition((PageSize.POSTCARD.getWidth() - img.getScaledWidth()) / 2, (PageSize.POSTCARD.getHeight() - img.getScaledHeight()) / 2);
        writer.getDirectContent().addImage(img);
        Paragraph p = new Paragraph("Foobar Film Festival", new Font(FontFamily.HELVETICA, 22));
        p.setAlignment(Element.ALIGN_CENTER);
        document.add(p);
        document.close();
    }
