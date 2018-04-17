    public void execute() {
        try {
            if (getValue("srcfile") == null) throw new InstantiationException("You need to choose a sourcefile");
            File tiff_file = (File) getValue("srcfile");
            if (getValue("destfile") == null) throw new InstantiationException("You need to choose a destination file");
            File pdf_file = (File) getValue("destfile");
            RandomAccessFileOrArray ra = new RandomAccessFileOrArray(tiff_file.getAbsolutePath());
            int comps = TiffImage.getNumberOfPages(ra);
            boolean adjustSize = false;
            Document document = new Document(PageSize.A4);
            float width = PageSize.A4.getWidth() - 40;
            float height = PageSize.A4.getHeight() - 120;
            if ("ORIGINAL".equals(getValue("pagesize"))) {
                Image img = TiffImage.getTiffImage(ra, 1);
                if (img.getDpiX() > 0 && img.getDpiY() > 0) {
                    img.scalePercent(7200f / img.getDpiX(), 7200f / img.getDpiY());
                }
                document.setPageSize(new Rectangle(img.getScaledWidth(), img.getScaledHeight()));
                adjustSize = true;
            } else if ("LETTER".equals(getValue("pagesize"))) {
                document.setPageSize(PageSize.LETTER);
                width = PageSize.LETTER.getWidth() - 40;
                height = PageSize.LETTER.getHeight() - 120;
            }
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdf_file));
            document.open();
            PdfContentByte cb = writer.getDirectContent();
            for (int c = 0; c < comps; ++c) {
                Image img = TiffImage.getTiffImage(ra, c + 1);
                if (img != null) {
                    if (img.getDpiX() > 0 && img.getDpiY() > 0) {
                        img.scalePercent(7200f / img.getDpiX(), 7200f / img.getDpiY());
                    }
                    if (adjustSize) {
                        document.setPageSize(new Rectangle(img.getScaledWidth(), img.getScaledHeight()));
                        document.newPage();
                        img.setAbsolutePosition(0, 0);
                    } else {
                        if (img.getScaledWidth() > width || img.getScaledHeight() > height) {
                            if (img.getDpiX() > 0 && img.getDpiY() > 0) {
                                float adjx = width / img.getScaledWidth();
                                float adjy = height / img.getScaledHeight();
                                float adj = Math.min(adjx, adjy);
                                img.scalePercent(7200f / img.getDpiX() * adj, 7200f / img.getDpiY() * adj);
                            } else img.scaleToFit(width, height);
                        }
                        img.setAbsolutePosition(20, 20);
                        document.newPage();
                        document.add(new Paragraph(tiff_file + " - page " + (c + 1)));
                    }
                    cb.addImage(img);
                    System.out.println("Finished page " + (c + 1));
                }
            }
            ra.close();
            document.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(internalFrame, e.getMessage(), e.getClass().getName(), JOptionPane.ERROR_MESSAGE);
            System.err.println(e.getMessage());
        }
    }
