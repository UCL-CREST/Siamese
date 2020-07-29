    public static void main(String[] args) {
        System.out.println("FontColor");
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("ChunkColor.pdf"));
            document.open();
            Font red = FontFactory.getFont(FontFactory.HELVETICA, Font.DEFAULTSIZE, Font.BOLD, new Color(0xFF, 0x00, 0x00));
            Font blue = FontFactory.getFont(FontFactory.HELVETICA, Font.DEFAULTSIZE, Font.ITALIC, new Color(0x00, 0x00, 0xFF));
            Paragraph p;
            p = new Paragraph("Roses are ");
            p.add(new Chunk("red", red));
            document.add(p);
            p = new Paragraph("Violets are ");
            p.add(new Chunk("blue", blue));
            document.add(p);
            BaseFont bf = FontFactory.getFont(FontFactory.COURIER).getCalculatedBaseFont(false);
            PdfContentByte cb = writer.getDirectContent();
            cb.beginText();
            cb.setColorFill(new Color(0x00, 0xFF, 0x00));
            cb.setFontAndSize(bf, 12);
            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "Grass is green", 250, 700, 0);
            cb.endText();
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
