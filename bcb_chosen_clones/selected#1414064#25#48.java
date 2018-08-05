    public static void main(String[] args) {
        System.out.println("Chapter 15: example AnnotatedImages");
        System.out.println("-> Creates a PDF with annotated images;");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> resources needed: foxdog.jpg (chapter 5) and iTextLogo.gif (chapter 10)");
        System.out.println("-> file generated in /results subdirectory:");
        System.out.println("   annotated_images.pdf");
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter15/annotated_images.pdf"));
            document.open();
            Image gif = Image.getInstance("resources/in_action/chapter10/iTextLogo.gif");
            gif.setAnnotation(new Annotation(0, 0, 0, 0, "http://www.lowagie.com/iText"));
            gif.setAbsolutePosition(30f, 750f);
            document.add(gif);
            Image jpeg = Image.getInstance("resources/in_action/chapter05/foxdog.jpg");
            jpeg.setAnnotation(new Annotation("picture", "quick brown fox jumps over the lazy dog", 0, 0, 0, 0));
            jpeg.setAbsolutePosition(120f, 550f);
            document.add(jpeg);
        } catch (Exception de) {
            de.printStackTrace();
        }
        document.close();
    }
