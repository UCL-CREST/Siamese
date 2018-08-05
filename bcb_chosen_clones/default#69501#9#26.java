    public static void main(String[] args) {
        System.out.println("Chapter 6 example 2: Adding a Gif, Jpeg and Png-file using filenames");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Chap0603.pdf"));
            HtmlWriter writer = HtmlWriter.getInstance(document, new FileOutputStream("Chap0603.html"));
            writer.setImagepath("../../images/kerstmis/");
            document.open();
            Image jpg = Image.getInstance("raf.jpg");
            jpg.scalePercent(50);
            document.add(jpg);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
