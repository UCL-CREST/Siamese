    public static void main(String[] args) {
        System.out.println("True Types (embedded)");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("truetype.pdf"));
            document.open();
            BaseFont bfComic = BaseFont.createFont("c:\\windows\\fonts\\comic.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(bfComic, 12);
            String text1 = "This is the quite popular True Type font 'Comic'.";
            String text2 = "Some greek characters: ΓΔζ";
            String text3 = "Some cyrillic characters: Ия";
            document.add(new Paragraph(text1, font));
            document.add(new Paragraph(text2, font));
            document.add(new Paragraph(text3, font));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
