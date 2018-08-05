    public void createScreenCapture() {
        try {
            Point pt = new Point();
            int width = 0;
            int height = 0;
            pt = wwd.getLocationOnScreen();
            width = wwd.getWidth();
            height = wwd.getHeight();
            if (height <= 0 || width <= 0) {
                JOptionPane.showInternalMessageDialog(this, "A Screenshot was not possible - too small of size", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
            BufferedImage screencapture = new Robot().createScreenCapture(new Rectangle(pt.x, pt.y, width, height));
            final JFileChooser fc = new JFileChooser();
            jsattrak.utilities.CustomFileFilter pngFilter = new jsattrak.utilities.CustomFileFilter("png", "*.png");
            fc.addChoosableFileFilter(pngFilter);
            jsattrak.utilities.CustomFileFilter jpgFilter = new jsattrak.utilities.CustomFileFilter("jpg", "*.jpg");
            fc.addChoosableFileFilter(jpgFilter);
            fc.setDialogTitle("Save Screenshot");
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String fileExtension = "png";
                if (fc.getFileFilter() == pngFilter) {
                    fileExtension = "png";
                }
                if (fc.getFileFilter() == jpgFilter) {
                    fileExtension = "jpg";
                }
                String extension = getExtension(file);
                if (extension != null) {
                    fileExtension = extension;
                } else {
                    file = new File(file.getAbsolutePath() + "." + fileExtension);
                }
                Exception e = SaveImageFile.saveImage(fileExtension, file, screencapture, 0.9f);
                if (e != null) {
                    System.out.println("ERROR SCREEN CAPTURE:" + e.toString());
                    return;
                }
            } else {
            }
        } catch (Exception e4) {
            System.out.println("ERROR SCREEN CAPTURE:" + e4.toString());
        }
    }
