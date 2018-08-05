    public static void main(String[] args) {
        System.out.println("Chapter 4: example FoxDogGeneric1");
        System.out.println("-> Creates a PDF file with the text");
        System.out.println("   'Quick brown fox jumps over the lazy dog';");
        System.out.println("   some chunks are tagged.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> resulting PDF: fox_dog_generic1.pdf");
        Document document = new Document();
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter04/fox_dog_generic1.pdf"));
            writer.setPageEvent(new FoxDogGeneric1());
            document.open();
            Paragraph p = new Paragraph();
            Chunk fox = new Chunk("Quick brown fox");
            fox.setGenericTag("box");
            p.add(fox);
            p.add(" jumps over ");
            Chunk dog = new Chunk("the lazy dog.");
            dog.setGenericTag("ellipse");
            p.add(dog);
            document.add(p);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
