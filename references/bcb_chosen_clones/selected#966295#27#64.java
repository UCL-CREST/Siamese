    public static void main(String[] args) {
        System.out.println("Chapter 8: example TrueTypeFontExample");
        System.out.println("-> Creates a PDF file with a not embedded TrueType font.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> extra resources needed: c:/windows/fonts/ARBLI___.TTF");
        System.out.println("-> file generated: ttf.pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter08/ttf.pdf"));
            document.open();
            BaseFont bf;
            Font font;
            bf = BaseFont.createFont("c:/windows/fonts/ARBLI___.TTF", BaseFont.CP1252, BaseFont.EMBEDDED);
            font = new Font(bf, 12);
            System.out.println(bf.getClass().getName());
            document.add(new Paragraph("This is font arial black italic (embedded)", font));
            bf = BaseFont.createFont("c:/windows/fonts/ARBLI___.TTF", BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
            font = new Font(bf, 12);
            document.add(new Paragraph("This is font arial black italic (not embedded)", font));
            document.add(new Paragraph("PostScript name:" + bf.getPostscriptFontName()));
            document.add(new Paragraph("Available code pages:"));
            String[] encoding = bf.getCodePagesSupported();
            for (int i = 0; i < encoding.length; i++) {
                document.add(new Paragraph("encoding[" + i + "] = " + encoding[i]));
            }
            document.newPage();
            document.add(new Paragraph("Full font names:"));
            String[][] name = bf.getFullFontName();
            for (int i = 0; i < name.length; i++) {
                document.add(new Paragraph(name[i][3] + " (" + name[i][0] + "; " + name[i][1] + "; " + name[i][2] + ")"));
            }
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
