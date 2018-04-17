    public void exportTreeImage(String path, int dims[]) {
        float oldLineWidth = getLineWidthScale();
        int oldWidth = getWidth();
        int oldHeight = getHeight();
        double oldXScale = xscale;
        double oldYScale = yscale;
        double oldXStart = xstart;
        double oldYStart = ystart;
        try {
            setLineWidthScale(oldLineWidth * (float) .2);
            xstart = 0;
            ystart = 0;
            width = dims[0];
            height = dims[1];
            TREEMARGIN = 0;
            if (drawExternalNodeLabels && zoomDrawNodeLabels) TREEMARGIN = textWidth(root.getLongestLabel());
            float usableWidth = 0;
            float usableHeight = 0;
            if (treeLayout.equals("Rectangular") || treeLayout.equals("Triangular")) {
                usableWidth = dims[0] - TREEMARGIN - 5;
                usableHeight = dims[1] - (float) MARGIN * 2 - 5;
                xscale = usableWidth / root.depth();
                xstart = MARGIN;
                yscale = usableHeight / root.getNumberOfLeaves();
                ystart = MARGIN;
            } else if (treeLayout.equals("Radial") || treeLayout.equals("Polar")) {
                usableWidth = dims[0] - 2 * TREEMARGIN - 5;
                usableHeight = dims[1] - 2 * TREEMARGIN - 5;
                xscale = (Math.min(usableWidth, usableHeight) * 0.5) / root.depth();
                xstart = dims[0] * 0.5;
                yscale = (Math.min(usableWidth, usableHeight) * 0.5) / root.depth();
                ystart = dims[1] * 0.5;
            }
            PGraphics canvas = createGraphics((int) (dims[0]), (int) (dims[1]), PDF, path);
            canvas.beginDraw();
            canvas.background(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue());
            canvas.pushMatrix();
            canvas.translate((float) xstart, (float) ystart);
            canvas.rotate((float) (treerotation * Math.PI / 180.0));
            canvas.translate((float) -xstart, (float) -ystart);
            canvas.textFont(nodeFont);
            drawTree(root, canvas);
            canvas.popMatrix();
            canvas.dispose();
            canvas.endDraw();
            if (Desktop.isDesktopSupported()) {
                try {
                    File myFile = new File(path);
                    Desktop.getDesktop().open(myFile);
                } catch (IOException ex) {
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unable to export pdf.\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        setLineWidthScale(oldLineWidth);
        xscale = oldXScale;
        yscale = oldYScale;
        xstart = oldXStart;
        ystart = oldYStart;
        width = oldWidth;
        height = oldHeight;
        redraw();
    }
