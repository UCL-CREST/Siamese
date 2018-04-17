    public void run() {
        try {
            Rectangle rec = fComponent.getBounds();
            Robot robot = new Robot();
            sleep(100);
            BufferedImage img = new Robot().createScreenCapture(rec);
            JFileChooser chooser = new JFileChooser(".");
            chooser.addChoosableFileFilter(new PngFilter());
            chooser.addChoosableFileFilter(new BmpFilter());
            chooser.addChoosableFileFilter(new JpgFilter());
            FileFilter[] filters = chooser.getChoosableFileFilters();
            chooser.setFileFilter(filters[0]);
            int returnVal = chooser.showSaveDialog(fComponent);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File selectedFile = chooser.getSelectedFile();
                if (!selectedFile.exists()) selectedFile.createNewFile();
                FileFilter ff = chooser.getFileFilter();
                String format = getExtention(selectedFile);
                if (format.equals("jpg") || format.equals("jpeg") || format.equals("png") || format.equals("bmp")) {
                    if (selectedFile.canWrite()) ImageIO.write(img, format, selectedFile.getAbsoluteFile()); else new ErrorFrame("IO Error on File " + selectedFile.getAbsoluteFile() + " occured");
                } else if (getExtention(selectedFile).equals("eps")) {
                    if (selectedFile.canWrite()) ImageIO.write(img, "eps", selectedFile.getAbsoluteFile()); else new ErrorFrame("IO Error on File " + selectedFile.getAbsoluteFile() + " occured");
                } else if (ff.getDescription().equals("All Files")) {
                    if (selectedFile.canWrite()) ImageIO.write(img, "png", new File(selectedFile.getAbsolutePath() + ".png")); else new ErrorFrame("IO Error on File " + selectedFile.getAbsoluteFile() + " occured");
                } else {
                    if (selectedFile.canWrite()) {
                        format = ff.getDescription().substring(2);
                        ImageIO.write(img, format, new File(selectedFile.getAbsoluteFile() + "." + format));
                    } else new ErrorFrame("IO Error on File " + selectedFile.getAbsoluteFile() + " occurred");
                }
            }
        } catch (Exception e) {
            new ErrorFrame("IO Error occurred while capturing the screen");
        }
    }
