    public static void main(String[] args) {
        System.out.println("Chapter 1 example 6: Meta Information");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Chap0106.pdf"));
            HtmlWriter.getInstance(document, System.out);
            document.addTitle("Hello World example");
            document.addSubject("This example explains step 3 in Chapter 1");
            document.addKeywords("Metadata, iText, step 3, tutorial");
            document.addAuthor("Bruno Lowagie");
            document.addHeader("Expires", "0");
            document.open();
            document.add(new Paragraph("Hello World"));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
