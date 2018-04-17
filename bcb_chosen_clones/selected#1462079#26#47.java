    public static void main(String[] args) {
        System.out.println("Chapter 9: example SymbolSubstitution");
        System.out.println("-> Creates a PDF file that uses a special Phrase constructor.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> file generated: symbol_substitution.pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter09/symbol_substitution.pdf"));
            document.open();
            String text = "What is the " + (char) 945 + "-coefficient of the " + (char) 946 + "-factor in the " + (char) 947 + "-equation?";
            document.add(Phrase.getInstance(text));
            document.add(Chunk.NEWLINE);
            for (int i = 913; i < 970; i++) {
                document.add(Phrase.getInstance(String.valueOf(i) + ": " + (char) i + " "));
            }
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
