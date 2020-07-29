    public static void main(String[] args) {
        System.out.println("Chapter 5: example FoxDogAnimatedGif");
        System.out.println("-> Creates a PDF file with images");
        System.out.println("   of a brown fox and a lazy dog.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> resources needed: animated_fox_dog.gif");
        System.out.println("-> resulting PDF: fox_dog_gif.pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter05/fox_dog_gif.pdf"));
            document.open();
            document.add(new Paragraph("This is the animated gif added with Image.getInstance:"));
            document.add(Image.getInstance("resources/in_action/chapter05/animated_fox_dog.gif"));
            GifImage img = new GifImage("resources/in_action/chapter05/animated_fox_dog.gif");
            int frames = img.getFrameCount();
            document.add(new Paragraph("There are " + frames + " frames in the animated gif file."));
            for (int i = 0; i < frames; ) {
                ++i;
                document.add(img.getImage(i));
            }
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
