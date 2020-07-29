    public static void main(String[] args) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(RESULT));
            document.open();
            Phrase p;
            Chunk separator = new Chunk(new LineSeparator(0.5f, 70, Color.RED, Element.ALIGN_CENTER, 3));
            for (int i = 0; i < 40; i++) {
                p = new Phrase("TEST");
                p.add(separator);
                p.add(new Chunk(String.valueOf(i)));
                p.add(separator);
                p.add(new Chunk(String.valueOf(i * 2)));
                p.add(separator);
                p.add(new Chunk(WORDS[39 - i]));
                p.add(separator);
                p.add(new Chunk(String.valueOf(i * 4)));
                p.add(separator);
                p.add(new Chunk(WORDS[i]));
                document.add(p);
                document.add(Chunk.NEWLINE);
            }
            document.close();
        } catch (Exception de) {
            de.printStackTrace();
        }
    }
