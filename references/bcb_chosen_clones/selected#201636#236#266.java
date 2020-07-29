    public void saveMyPreviewSelf(ZipOutputStream out) throws IOException {
        BufferedImage bi = new BufferedImage(getMinimumSize().width, getMinimumSize().height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) bi.createGraphics();
        g.setPaint(Color.BLACK);
        g.setStroke(new BasicStroke(3f));
        int drawHeight = 5;
        for (int i = 0; i < covers.length; i++) {
            if (getComponent(i) != null) {
                AffineTransform oldTransform = g.getTransform();
                float x = ((float) (getMinimumSize().width - ((JComponent) getComponent(i)).getPreferredSize().width)) / 2f;
                float y = (float) drawHeight;
                g.translate(x, y);
                covers[i].setStrokeSize(3f);
                boolean gr = covers[i].getGrayscale();
                covers[i].setGrayscale(false);
                g.drawImage(covers[i].getBufferedImage(), 0, 0, this);
                covers[i].setGrayscale(gr);
                covers[i].setStrokeSize(0.25f);
                g.drawRect(0, 0, covers[i].getBufferedImage().getWidth(), covers[i].getBufferedImage().getHeight());
                g.setTransform(oldTransform);
                drawHeight += ((JComponent) getComponent(i)).getPreferredSize().height + 10;
            }
        }
        float scaleFactor = (((float) 150) / ((float) bi.getWidth()) + ((float) 150) / ((float) bi.getHeight())) / 2f;
        AffineTransform scaled = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
        AffineTransformOp scaledTransform = new AffineTransformOp(scaled, new RenderingHints(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED));
        bi = scaledTransform.filter(bi, null);
        out.putNextEntry(new ZipEntry("coverPreview_image.png"));
        ImageIO.write(bi, "png", out);
        out.closeEntry();
    }
