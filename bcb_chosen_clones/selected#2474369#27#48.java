    public static void main(String[] args) {
        System.out.println("Chapter 8: example CompactFontFormatExample");
        System.out.println("-> Creates a PDF file with a compact font format font.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> resources needed: esl_gothic_shavian.otf");
        System.out.println("-> file generated: cff.pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter08/cff.pdf"));
            document.open();
            BaseFont bf = BaseFont.createFont("resources/in_action/chapter08/esl_gothic_shavian.otf", "Cp1252", BaseFont.EMBEDDED);
            System.out.println(bf.getClass().getName());
            Font font = new Font(bf, 12);
            document.add(new Paragraph("All human beings are born free and equal in dignity and rights. " + "They are endowed with reason and conscience and should act towards one another in a spirit of brotherhood."));
            document.add(new Paragraph("Yl hVman bIiNz R bPn frI n ikwal in dignitI n rFts. " + "Hej R endQd wiH rIzn n konSans n Sud Akt tawPds wan anaHr in a spirit ov braHarhUd.", font));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
