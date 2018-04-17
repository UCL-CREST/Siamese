    public static void main(String[] args) {
        System.out.println("the Chunk object");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Chunks.pdf"));
            document.open();
            Chunk fox = new Chunk("quick brown fox");
            float superscript = 8.0f;
            fox.setTextRise(superscript);
            fox.setBackground(new Color(0xFF, 0xDE, 0xAD));
            Chunk jumps = new Chunk(" jumps over ");
            Chunk dog = new Chunk("the lazy dog");
            float subscript = -8.0f;
            dog.setTextRise(subscript);
            dog.setUnderline(new Color(0xFF, 0x00, 0x00), 3.0f, 0.0f, -5.0f + subscript, 0.0f, PdfContentByte.LINE_CAP_ROUND);
            document.add(fox);
            document.add(jumps);
            document.add(dog);
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
