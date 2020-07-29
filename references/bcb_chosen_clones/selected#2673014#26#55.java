    public static void main(String[] args) {
        System.out.println("Chapter 4: example FoxDogImageScaling2");
        System.out.println("-> Creates a PDF file with images");
        System.out.println("   of a brown fox and a lazy dog.");
        System.out.println("-> jars needed: iText.jar");
        System.out.println("-> resources needed: foxdog.tiff");
        System.out.println("-> resulting PDF: fox_dog_image_scaling2.pdf");
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("results/in_action/chapter05/fox_dog_image_scaling2.pdf"));
            document.open();
            Image tiff = Image.getInstance("resources/in_action/chapter05/foxdog.tiff");
            document.add(tiff);
            document.add(new Paragraph("Original width: " + tiff.getWidth() + "; original height: " + tiff.getHeight()));
            document.add(new Paragraph("DPI X: " + tiff.getDpiX() + "; DPI Y: " + tiff.getDpiY()));
            tiff.scalePercent(72f / tiff.getDpiX() * 100);
            document.add(new Paragraph("Show the image with 360 Dpi (scaled " + (7200f / tiff.getDpiX()) + "%):"));
            document.add(tiff);
            document.add(new Paragraph("Scaled width: " + tiff.getScaledWidth() + "; scaled height: " + tiff.getScaledHeight()));
            tiff.scaleToFit(200, 200);
            document.add(tiff);
            document.add(new Paragraph("Scaled width: " + tiff.getScaledWidth() + "; scaled height: " + tiff.getScaledHeight()));
            document.add(new Paragraph("DPI X: " + (72f * tiff.getWidth() / tiff.getScaledWidth()) + "; DPI Y: " + (72f * tiff.getHeight() / tiff.getScaledHeight())));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        document.close();
    }
