    public void screenshot() {
        JFileChooser chooser = new JFileChooser(Preferences.getScreenshotPath());
        String fn;
        chooser.setFileFilter(new PNGFileFilter());
        int r = chooser.showSaveDialog(JLad.getMainWindow());
        if (r != JFileChooser.APPROVE_OPTION) return;
        try {
            fn = chooser.getSelectedFile().getCanonicalPath();
            Preferences.setScreenshotPath(chooser.getCurrentDirectory().getAbsolutePath());
        } catch (IOException e) {
            Debug.msg("IOException while saving a screenshot: " + e.getMessage() + "\n");
            Dialogues.error("Could not save the screenshot!");
            return;
        } catch (SecurityException e) {
            Debug.msg("SecurityException while saving a screenshot: " + e.getMessage() + "\n");
            Dialogues.error("Could not save the screenshot!");
            return;
        }
        if (!fn.endsWith(".png")) fn = fn.concat(".png");
        File file = new File(fn);
        int type = BufferedImage.TYPE_INT_RGB;
        Component c;
        if (this.gameGrid != null) c = this.gameGrid; else if (this.topList != null) c = this.topList; else {
            Debug.msg("Something weird going on in MainWindow.screenshot()\n");
            Dialogues.error("Could not save the screenshot!");
            return;
        }
        int w = c.getWidth();
        int h = c.getHeight();
        try {
            BufferedImage image = new BufferedImage(w, h, type);
            c.paint(image.getGraphics());
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            Debug.msg("IOExceptien while saving a screenshot: " + e.getMessage() + "\n");
            Dialogues.error("Could not save the screenshot!");
        }
    }
