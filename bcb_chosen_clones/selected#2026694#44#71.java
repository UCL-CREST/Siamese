    private byte[] createPdf(Rectangle recToDraw) throws DocumentException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document doc = new Document();
        PdfWriter writer = PdfWriter.getInstance(doc, baos);
        writer.setCompressionLevel(0);
        doc.open();
        PdfContentByte canvas = writer.getDirectContent();
        canvas.beginText();
        float fontsiz = 12;
        float llx = 1.42f * 72f;
        float lly = 2.42f * 72f;
        float urx = 7.42f * 72f;
        float ury = 10.42f * 72f;
        BaseFont font = BaseFont.createFont();
        canvas.setFontAndSize(font, fontsiz);
        float ascent = font.getFontDescriptor(BaseFont.ASCENT, fontsiz);
        float descent = font.getFontDescriptor(BaseFont.DESCENT, fontsiz);
        canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, "LowerLeft", llx, lly - descent, 0.0f);
        canvas.showTextAligned(PdfContentByte.ALIGN_RIGHT, "LowerRight", urx, lly - descent, 0.0f);
        canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, "UpperLeft", llx, ury - ascent, 0.0f);
        canvas.showTextAligned(PdfContentByte.ALIGN_RIGHT, "UpperRight", urx, ury - ascent, 0.0f);
        canvas.endText();
        if (recToDraw != null) {
            doc.add(recToDraw);
        }
        doc.close();
        return baos.toByteArray();
    }
