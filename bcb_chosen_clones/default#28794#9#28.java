    public static void main(String[] args) {
        System.out.println("Chapter 6 example 2: Adding a Gif, Jpeg and Png-file using filenames");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Chap0602.pdf"));
            HtmlWriter.getInstance(document, new FileOutputStream("Chap0602.html"));
            document.open();
            Image gif = Image.getInstance("vonnegut.gif");
            Image jpeg = Image.getInstance("myKids.jpg");
            Image png = Image.getInstance("hitchcock.png");
            document.add(gif);
            document.add(jpeg);
            document.add(png);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
