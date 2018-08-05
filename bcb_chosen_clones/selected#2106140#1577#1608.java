    public void saveImageAs() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setPreferredSize(new Dimension(400, 300));
        fileChooser.setCurrentDirectory(new File(iSaveImageAsDir));
        fileChooser.setSelectedFile(new File(iLastSaveAsName));
        String path = "";
        int returnVal = fileChooser.showSaveDialog(iAceTree);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            path = file.getPath();
            iSaveImageAsDir = file.getParent();
            iLastSaveAsName = file.getName() + "x";
        } else {
            System.out.println("Save command cancelled by user.");
        }
        Rectangle screenRect = iFrame.getBounds();
        int topAdjust = 23;
        int y = screenRect.y;
        screenRect.y += topAdjust;
        int height = screenRect.height;
        screenRect.height -= topAdjust;
        Robot robot = null;
        try {
            robot = new Robot();
            BufferedImage image = robot.createScreenCapture(screenRect);
            ImageIO.write(image, IMAGETYPE, new File(path + "." + IMAGETYPE));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("file: " + path + " written");
        iSaveInProcess = false;
    }
