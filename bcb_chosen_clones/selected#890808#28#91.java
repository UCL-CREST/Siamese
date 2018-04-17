    public static void main(String[] args) {
        System.out.println("Chapter 8: example TrueTypeFontEncoding");
        System.out.println("-> Creates a PDF file with Type1 font.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> extra resource needed: c:/windows/fonts/arialbd.ttf");
        System.out.println("-> file generated: ttf_encoding.pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter08/ttf_encoding.pdf"));
            document.open();
            BaseFont bf;
            Font font;
            bf = BaseFont.createFont("c:/windows/fonts/arialbd.ttf", "Cp1252", BaseFont.EMBEDDED);
            System.out.println(bf.getClass().getName());
            document.add(new Paragraph("Font: " + bf.getPostscriptFontName()));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Movie title: A Very long Engagement (France)"));
            document.add(new Paragraph("directed by Jean-Pierre Jeunet"));
            document.add(new Paragraph("Encoding: " + bf.getEncoding()));
            font = new Font(bf, 12);
            document.add(new Paragraph("Un long dimanche de fiançailles", font));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Movie title: No Man's Land (Bosnia-Herzegovina)"));
            document.add(new Paragraph("Directed by Danis Tanovic"));
            bf = BaseFont.createFont("c:/windows/fonts/arialbd.ttf", "Cp1250", BaseFont.EMBEDDED);
            document.add(new Paragraph("Encoding: " + bf.getEncoding()));
            font = new Font(bf, 12);
            String noMansLand = "Nikogaršnja zemlja";
            document.add(new Paragraph(noMansLand, font));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Movie title: You I Love (Russia)"));
            bf = BaseFont.createFont("c:/windows/fonts/arialbd.ttf", "Cp1251", BaseFont.EMBEDDED);
            document.add(new Paragraph("directed by Olga Stolpovskaja and Dmitry Troitsky"));
            document.add(new Paragraph("Encoding: " + bf.getEncoding()));
            font = new Font(bf, 12);
            char[] youILove = { 1071, ' ', 1083, 1102, 1073, 1083, 1102, ' ', 1090, 1077, 1073, 1103 };
            document.add(new Paragraph(new String(youILove), font));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Movie title: Brides (Greece)"));
            document.add(new Paragraph("directed by Pantelis Voulgaris"));
            bf = BaseFont.createFont("c:/windows/fonts/arialbd.ttf", "Cp1253", BaseFont.EMBEDDED);
            document.add(new Paragraph("Encoding: " + bf.getEncoding()));
            font = new Font(bf, 12);
            byte[] brides = { -51, -3, -10, -27, -14 };
            document.add(new Paragraph(new String(brides, "Cp1253"), font));
            document.newPage();
            document.add(new Paragraph("Available code pages"));
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
