            @Override
            public void actionPerformed(ActionEvent e) {
                Robot r;
                try {
                    Rectangle bounds = new Rectangle(GrabberWindow.getWindow().getLocationOnScreen().x, GrabberWindow.getWindow().getLocationOnScreen().y, GrabberWindow.getWindow().getWidth(), GrabberWindow.getWindow().getHeight());
                    GrabberWindow.getWindow().setVisible(false);
                    r = new Robot();
                    BufferedImage tmpImg = r.createScreenCapture(new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height));
                    String dir = OptionsEngine.getOptions().get(Options.Directory.getID()).toString();
                    if (dir.equals("")) {
                        JFileChooser fc = new JFileChooser(lastDir);
                        FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                        fc.setFileFilter(filter);
                        int returnVal = fc.showSaveDialog(GrabberWindow.getWindow().getContentPane());
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            String path = fc.getSelectedFile().getAbsolutePath();
                            if (!path.toLowerCase().endsWith(".png")) path += ".png";
                            lastDir = path;
                            ImageIO.write(tmpImg, "png", new File(path));
                        }
                    } else {
                        String name = "img";
                        int i = 1;
                        while (new File(dir + "\\" + name + i + ".png").exists()) i++;
                        ImageIO.write(tmpImg, "png", new File(dir + "\\" + name + i + ".png"));
                    }
                } catch (AWTException e1) {
                    e1.printStackTrace();
                } catch (IOException e2) {
                    e2.printStackTrace();
                } finally {
                    GrabberWindow.getWindow().setVisible(true);
                }
            }
