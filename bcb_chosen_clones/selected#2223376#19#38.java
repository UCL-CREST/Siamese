    public static void main(String[] p) {
        AAAJpg t = new AAAJpg();
        try {
            GB_Frame l_frame = new GB_Frame();
            GB_PanelRowsTitle l_panel = new GB_PanelRowsTitle();
            l_panel.addRow("toto", new JLabel("abcd"));
            l_panel.addRowFill("toto", new JTextField("abcd"));
            l_frame.setMainPanel(l_panel);
            l_frame.pack();
            l_frame.setVisible(true);
            Thread.sleep(2000);
            Robot r = new Robot();
            Dimension d = l_frame.getSize();
            BufferedImage l_img = r.createScreenCapture(new Rectangle(0, 0, d.width, d.height));
            File f = new File("c:/aa/toto.png");
            ImageIO.write(l_img, "png", f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
