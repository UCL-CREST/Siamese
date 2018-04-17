    public static void main(String[] args) {
        System.out.println("Chapter 11: example Transparency3");
        System.out.println("-> Creates a PDF file with a transparent image (soft mask).");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> extra resource needed: foxdog.jpg (chapter 5)");
        System.out.println("-> file generated: transparency3.pdf");
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter11/transparency3.pdf"));
            document.open();
            Paragraph text = new Paragraph("Quick brown fox jumps over the lazy dog.", new Font(Font.HELVETICA, 18));
            for (int i = 0; i < 10; i++) document.add(text);
            Image img = Image.getInstance("resources/in_action/chapter05/foxdog.jpg");
            img.setAbsolutePosition(50, 550);
            byte gradient[] = new byte[256];
            for (int k = 0; k < 256; ++k) gradient[k] = (byte) k;
            Image smask = Image.getInstance(256, 1, 1, 8, gradient);
            smask.makeMask();
            img.setImageMask(smask);
            writer.getDirectContent().addImage(img);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
