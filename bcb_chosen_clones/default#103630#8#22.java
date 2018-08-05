    public static void main(String[] args) {
        System.out.println("Chapter 2 example 4: Negative leading");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Chap0204.pdf"));
            document.open();
            document.add(new Phrase(16, "\n\n\n"));
            document.add(new Phrase(-16, "Hello, this is a very long phrase to show you the somewhat odd effect of a negative leading. You can write from bottom to top. This is not fully supported. It's something between a feature and a bug."));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
