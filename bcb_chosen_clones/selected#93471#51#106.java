    public void execute() {
        try {
            if (getValue("srcdir") == null) throw new InstantiationException("You need to choose a source directory");
            File directory = (File) getValue("srcdir");
            if (directory.isFile()) directory = directory.getParentFile();
            if (getValue("destfile") == null) throw new InstantiationException("You need to choose a destination file");
            File pdf_file = (File) getValue("destfile");
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdf_file));
            writer.setViewerPreferences(PdfWriter.PageModeUseThumbs);
            PdfPageLabels pageLabels = new PdfPageLabels();
            int dpiX, dpiY;
            float imgWidthPica, imgHeightPica;
            TreeSet images = new TreeSet();
            File[] files = directory.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) images.add(files[i]);
            }
            File image;
            for (Iterator i = images.iterator(); i.hasNext(); ) {
                image = (File) i.next();
                System.out.println("Testing image: " + image.getName());
                try {
                    Image img = Image.getInstance(image.getAbsolutePath());
                    dpiX = img.getDpiX();
                    if (dpiX == 0) dpiX = 72;
                    dpiY = img.getDpiY();
                    if (dpiY == 0) dpiY = 72;
                    imgWidthPica = (72 * img.plainWidth()) / dpiX;
                    imgHeightPica = (72 * img.plainHeight()) / dpiY;
                    img.scaleAbsolute(imgWidthPica, imgHeightPica);
                    document.setPageSize(new Rectangle(imgWidthPica, imgHeightPica));
                    if (document.isOpen()) {
                        document.newPage();
                    } else {
                        document.open();
                    }
                    img.setAbsolutePosition(0, 0);
                    document.add(img);
                    pageLabels.addPageLabel(writer.getPageNumber(), PdfPageLabels.EMPTY, image.getName());
                    System.out.println("Added image: " + image.getName());
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            if (document.isOpen()) {
                writer.setPageLabels(pageLabels);
                document.close();
            } else {
                System.err.println("No images were found in directory " + directory.getAbsolutePath());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(internalFrame, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            System.err.println(e.getMessage());
        }
    }
