    public static void main(String[] args) {
        System.out.println("Chapter 6 example 1: Adding a Gif, Jpeg and Png-file using urls");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Chap0601.pdf"));
            HtmlWriter.getInstance(document, new FileOutputStream("Chap0601.html"));
            document.open();
            Image gif = Image.getInstance(new URL("http://www.lowagie.com/iText/tutorial/vonnegut.gif"));
            Image jpeg = Image.getInstance(new URL("http://www.lowagie.com/iText/tutorial/myKids.jpg"));
            Image png = Image.getInstance(new URL("http://www.lowagie.com/iText/tutorial/hitchcock.png"));
            document.add(gif);
            document.add(jpeg);
            document.add(png);
        } catch (MalformedURLException mue) {
            System.err.println(mue.getMessage());
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
