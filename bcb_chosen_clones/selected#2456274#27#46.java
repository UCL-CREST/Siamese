    public static void main(String[] args) {
        System.out.println("Chapter 8: example Type1FontFromPFBwithPFM");
        System.out.println("-> Creates a PDF file with Type1 font.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("   extra resources needed: cmr10.pfm and cmr10.pfb");
        System.out.println("-> file generated: type1_font_pfb_with_pfm.pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter08/type1_font_pfb_with_pfm.pdf"));
            document.open();
            BaseFont bf = BaseFont.createFont("resources/in_action/chapter08/cmr10.pfm", "", BaseFont.EMBEDDED);
            Font font = new Font(bf, 12);
            document.add(new Paragraph("0123456789\nabcdefghijklmnopqrstuvwxyz\nABCDEFGHIJKLMNOPQRSTUVWXZ", font));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
