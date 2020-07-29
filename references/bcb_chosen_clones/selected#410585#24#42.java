    public static void main(String[] args) {
        System.out.println("Chapter 2: example HelloWorldLandscape2");
        System.out.println("-> Creates a PDF file with the text 'Hello World';");
        System.out.println("   a Rectangle with width > height was used as PageSize.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> files generated in /results subdirectory:");
        System.out.println("   HelloWorldLandscape2.pdf");
        Document document = new Document(new Rectangle(792, 612));
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter02/HelloWorldLandscape2.pdf"));
            document.open();
            document.add(new Paragraph("Hello World"));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
