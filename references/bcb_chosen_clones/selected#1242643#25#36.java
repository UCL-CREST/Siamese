    public void createPdf(String filename) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filename));
        document.open();
        BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.CP1252, BaseFont.EMBEDDED);
        Font font = new Font(bf, 12);
        document.add(new Paragraph("Movie title: Love at First Hiccough (Denmark)", font));
        document.add(new Paragraph("directed by Tomas Villum Jensen", font));
        document.add(new Paragraph("Kærlighed ved første hik", font));
        document.add(new Paragraph(ligaturize("Kaerlighed ved f/orste hik"), font));
        document.close();
    }
