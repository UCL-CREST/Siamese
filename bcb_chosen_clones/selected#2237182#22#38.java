    public static void main(String[] args) {
        String string = "Look at this paragraph with a lot of different products and at least one product called Kautschuk-Plant, where the plant itself can be planted, on the other hand, there are some more words with absolutely no sense.";
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapterX/hyphen_not_hyphenated.pdf"));
            document.open();
            document.add(new Paragraph(string));
            Chunk c = new Chunk(string);
            c.setSplitCharacter(new NonHyphenatingHyphen());
            document.add(new Paragraph(c));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
