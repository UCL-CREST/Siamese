    public static void main(String[] args) {
        System.out.println("Chapter 6 example 5: Alignment of images");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Chap0605.pdf"));
            document.open();
            Image gif = Image.getInstance("vonnegut.gif");
            gif.setAlignment(Image.RIGHT | Image.TEXTWRAP);
            Image jpeg = Image.getInstance("myKids.jpg");
            jpeg.setAlignment(Image.MIDDLE);
            Image png = Image.getInstance("hitchcock.png");
            png.setAlignment(Image.LEFT | Image.UNDERLYING);
            for (int i = 0; i < 100; i++) {
                document.add(new Phrase("Who is this? "));
            }
            document.add(gif);
            for (int i = 0; i < 100; i++) {
                document.add(new Phrase("Who is this? "));
            }
            document.add(jpeg);
            for (int i = 0; i < 100; i++) {
                document.add(new Phrase("Who is this? "));
            }
            document.add(png);
            for (int i = 0; i < 100; i++) {
                document.add(new Phrase("Who is this? "));
            }
            document.add(gif);
            for (int i = 0; i < 100; i++) {
                document.add(new Phrase("Who is this? "));
            }
            document.add(jpeg);
            for (int i = 0; i < 100; i++) {
                document.add(new Phrase("Who is this? "));
            }
            document.add(png);
            for (int i = 0; i < 100; i++) {
                document.add(new Phrase("Who is this? "));
            }
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
