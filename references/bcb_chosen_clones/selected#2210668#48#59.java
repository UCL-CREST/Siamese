    private static byte[] createPdf(final Font font) throws Exception {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final Document document = new Document();
        PdfWriter.getInstance(document, byteStream);
        document.open();
        document.add(new Paragraph(TEXT1, font));
        document.newPage();
        document.add(new Paragraph(TEXT2, font));
        document.close();
        final byte[] pdfBytes = byteStream.toByteArray();
        return pdfBytes;
    }
