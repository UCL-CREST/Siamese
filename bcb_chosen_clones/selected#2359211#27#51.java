    public static void main(String[] args) {
        System.out.println("Chapter 9: example Ligatures1");
        System.out.println("-> Creates a PDF file with a ligaturize method.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> resources needed: arial.ttf");
        System.out.println("-> file generated: ligatures1.pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter09/ligatures1.pdf"));
            document.open();
            BaseFont bf;
            Font font;
            bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.CP1252, BaseFont.EMBEDDED);
            font = new Font(bf, 12);
            document.add(new Paragraph("Movie title: Love at First Hiccough (Denmark)", font));
            document.add(new Paragraph("directed by Tomas Villum Jensen", font));
            document.add(new Paragraph("Kærlighed ved første hik", font));
            document.add(new Paragraph(ligaturize("Kaerlighed ved f/orste hik"), font));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
