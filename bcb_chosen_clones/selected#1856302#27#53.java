    public static void main(String[] args) {
        System.out.println("Chapter 8: example FontMetrics");
        System.out.println("-> Creates a PDF file with text in font Helvetica.");
        System.out.println("   Different metrics are measured.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> file generated: font_metrics.pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter08/font_metrics.pdf"));
            document.open();
            Font font = new Font(Font.HELVETICA, 12);
            BaseFont bf = font.getCalculatedBaseFont(false);
            String numbers = "0123456789";
            String letters = "abcdefghijklmnopqrstuvwxyz";
            document.add(new Paragraph(numbers, font));
            document.add(new Paragraph("width: " + bf.getWidth(numbers) + " (" + bf.getWidthPoint(numbers, 12) + "pt)", font));
            document.add(new Paragraph("ascent: " + bf.getAscent(numbers) + "; descent: " + bf.getDescent(numbers) + "; height: " + (bf.getAscentPoint(numbers, 12) - bf.getDescentPoint(numbers, 12) + "pt"), font));
            document.add(new Paragraph(letters, font));
            document.add(new Paragraph("width: " + bf.getWidth(letters) + " (" + bf.getWidthPoint(letters, 12) + "pt)", font));
            document.add(new Paragraph("ascent: " + bf.getAscent(letters) + "; descent: " + bf.getDescent(letters) + "; height: " + (bf.getAscentPoint(letters, 12) - bf.getDescentPoint(letters, 12)) + "pt", font));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
