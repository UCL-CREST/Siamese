    public void run() {
        GUI gui = (GUI) registeredObject;
        GPane pane = gui.getTopPane();
        if (pane.getMouseRectanglePosition() != null) {
            String extension = "png";
            String filename = File.separator + extension;
            JFrame frame = new JFrame();
            JFileChooser fileChooser = new JFileChooser(new File(filename));
            fileChooser.showSaveDialog(frame);
            File outputFile = fileChooser.getSelectedFile();
            Rectangle selection = pane.getMouseRectanglePosition();
            Point upperLeftScreenPixel = pane.getUpperLeftPixel();
            Point upperLeft = new Point(upperLeftScreenPixel.x + selection.x, upperLeftScreenPixel.y + selection.y);
            Point lowerRight = new Point(upperLeft.x + selection.width, upperLeft.y + selection.height);
            BufferedImage loadedImage = LibGUI.loadImage(outputFile.getPath());
            if (outputFile != null && loadedImage != null) gui.getGMap().getGDraw().add(new GImage(new GPhysicalPoint(upperLeft, pane.getZoom()), new GPhysicalPoint(lowerRight, pane.getZoom()), loadedImage));
        }
        pane.setMode(GPane.IMAGE_MODE);
        super.setSelected(true);
        gui.getNotifier().firePaneEvent(this);
        gui.getProgressMeter().getPanel().repaint();
    }
