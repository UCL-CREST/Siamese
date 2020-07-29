    public static void main(String[] args) {
        System.out.println("Chapter 2: example HelloWorldLandscape");
        System.out.println("-> Creates a PDF file with the text 'Hello World';");
        System.out.println("   PageSize.LETTER was used for the page size,");
        System.out.println("   the orientation was set to landscape.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> files generated in /results subdirectory:");
        System.out.println("   HelloWorldLandscape.pdf");
        Document document = new Document(PageSize.LETTER.rotate());
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter02/HelloWorldLandscape.pdf"));
            document.open();
            document.add(new Paragraph("Hello World"));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
