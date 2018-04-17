    public static void main(String[] args) {
        System.out.println("Chapter 6 example 7: Scaling an Image");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Chap0607.pdf"));
            document.open();
            Image jpg1 = Image.getInstance("myKids.jpg");
            jpg1.scaleAbsolute(97, 101);
            document.add(new Paragraph("scaleAbsolute(97, 101)"));
            document.add(jpg1);
            Image jpg2 = Image.getInstance("myKids.jpg");
            jpg2.scalePercent(50);
            document.add(new Paragraph("scalePercent(50)"));
            document.add(jpg2);
            Image jpg3 = Image.getInstance("myKids.jpg");
            jpg3.scaleAbsolute(194, 101);
            document.add(new Paragraph("scaleAbsolute(194, 101)"));
            document.add(jpg3);
            Image jpg4 = Image.getInstance("myKids.jpg");
            jpg4.scalePercent(100, 50);
            document.add(new Paragraph("scalePercent(100, 50)"));
            document.add(jpg4);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
