    private void saveImage(File outputFile) {
        String fileSuffix = FileUtilities.getSuffix(outputFile);
        if (ImageUtilities.isWriteSupportedFormat(fileSuffix)) {
            Robot robot = null;
            try {
                robot = new Robot();
            } catch (AWTException e) {
                JOptionPane.showMessageDialog(null, I18NHelper.getInstance().getString("action.savemapextract.creationerror"), I18NHelper.getInstance().getString("generic.error"), JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
            MapComponent mapComponent = mainWindow.getMapComponent();
            Point componentLocation = mapComponent.getLocationOnScreen();
            Dimension dimension = new Dimension(mapComponent.getWidth(), mapComponent.getHeight() - mapComponent.STATUS_BAR_HEIGHT);
            Rectangle rectangle = new Rectangle(componentLocation, dimension);
            BufferedImage image = robot.createScreenCapture(rectangle);
            try {
                ImageIO.write(image, fileSuffix, outputFile);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, I18NHelper.getInstance().getString("action.savemapextract.ioerror"), I18NHelper.getInstance().getString("generic.error"), JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, I18NHelper.getInstance().getString("action.savemapextract.unsupportedtype.msg"), I18NHelper.getInstance().getString("action.savemapextract.unsupportedtype.title"), JOptionPane.ERROR_MESSAGE);
            performAction();
        }
    }
