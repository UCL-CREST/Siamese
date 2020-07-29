    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        actualiser();
        if (source == Rectang || source == rectangle) {
            panel_1.setlwn(c);
            annuler.setEnabled(true);
            barre1.removeAll();
            barre1.add(rect_draw);
            barre1.add(rd_rect_draw);
            panel_1.flag = "rect_draw";
            barre1.repaint();
        }
        if (source == rect_draw) {
            panel_1.setlwn(c);
            annuler.setEnabled(true);
            barre1.removeAll();
            barre1.add(rect_draw);
            barre1.add(rd_rect_draw);
            panel_1.flag = "rect_draw";
            barre1.repaint();
        }
        if (source == rect_draw) {
            panel_1.setlwn(c);
            setCursor(JFrame.CROSSHAIR_CURSOR);
            panel_1.flag = "rect_draw";
        }
        if (source == rd_rect_draw) {
            panel_1.setlwn(c);
            setCursor(JFrame.CROSSHAIR_CURSOR);
            panel_1.flag = "rd_rect_draw";
        }
        if (source == apropos) panel_1.msgpropos();
        if (source == doc) {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    try {
                        desktop.open(new File("dist\\javadoc\\index.html"));
                    } catch (IOException ex) {
                    }
                }
            }
        }
        if ((source == Triangle) || (source == triangle)) {
            panel_1.setlwn(c);
            annuler.setEnabled(true);
            barre1.removeAll();
            barre1.add(Triangle_draw);
            panel_1.flag = "triangle_draw";
        }
        if ((source == Triangle_draw)) {
            panel_1.setlwn(c);
            panel_1.flag = "triangle_draw";
        }
        if ((source == Main) || (source == main)) {
            panel_1.setlwn(c);
            annuler.setEnabled(true);
            panel_1.flag = "taille1";
            barre1.removeAll();
            barre1.add(taille1);
            barre1.add(taille2);
            barre1.add(taille3);
            barre1.add(taille4);
            barre1.repaint();
        }
        if (source == taille1) {
            panel_1.flag = "taille1";
        }
        if (source == taille2) {
            panel_1.flag = "taille2";
        }
        if (source == taille3) {
            panel_1.flag = "taille3";
        }
        if (source == taille4) {
            panel_1.flag = "taille4";
        }
        if (source == Deplacer || source == deplacer) {
            setCursor(JFrame.MOVE_CURSOR);
            annuler.setEnabled(true);
            barre1.removeAll();
            barre1.add(vide1);
            barre1.add(vide2);
            barre1.repaint();
            panel_1.flag = "deplacer";
        }
        if (source == color_r) {
            c = JColorChooser.showDialog(p2, "S�lection...", getBackground());
            panel_1.setlwn(c);
            color_r.setBackground(c);
        }
        if (source == Ligne || source == ligne) {
            panel_1.setlwn(c);
            setCursor(JFrame.CROSSHAIR_CURSOR);
            annuler.setEnabled(true);
            panel_1.flag = "tligne1";
            barre1.removeAll();
            barre1.add(tligne1);
            barre1.add(tligne2);
            barre1.add(tligne3);
            barre1.add(tligne4);
            barre1.repaint();
        }
        if (source == tligne3) {
            panel_1.flag = "tligne3";
        }
        if (source == tligne1) {
            panel_1.flag = "tligne1";
        }
        if (source == tligne2) {
            panel_1.flag = "tligne2";
        }
        if (source == tligne4) {
            panel_1.flag = "tligne4";
        }
        if ((source == Polygone) || (source == Polygone)) {
            panel_1.setlwn(c);
            annuler.setEnabled(true);
            barre1.removeAll();
            barre1.add(premier_p);
            barre1.add(arriere_p);
            panel_1.flag = "polygone";
        }
        if (source == Ellipse || source == ellipse) {
            panel_1.setlwn(c);
            setCursor(JFrame.CROSSHAIR_CURSOR);
            annuler.setEnabled(true);
            panel_1.flag = "ellipse_draw";
            barre1.removeAll();
            barre1.add(ellipse_draw);
            barre1.repaint();
        }
        if (source == ellipse_draw) {
            panel_1.setlwn(c);
            panel_1.flag = "ellipse_draw";
        }
        if (source == Quitter) {
            int confirmation = JOptionPane.showConfirmDialog(contenu, "Etes-vous sur de vouloir quitter ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                dispose();
                System.exit(0);
            }
        }
        if (source == houghspace) {
            String filename = "vase.png";
            BufferedImage bimg = toBufferedImage(getImage(panel_1));
            Hough hough, param;
            try {
                panel_1.saveImage(bimg, new FileOutputStream("enter.png"));
                BufferedImage image = javax.imageio.ImageIO.read(new File(filename));
                hough = new Hough(bimg);
                hough.run(bimg);
                ImageIcon Imc = new ImageIcon("droite.png");
                JLabel label1 = new JLabel(Imc);
                contenu2.add(label1);
                contenu2.setVisible(true);
                contenu2.repaint();
                label1.repaint();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            frame.dispose();
            System.out.print("\n D�tection des droites ");
        }
        if (source == linespace) {
            String file = "espace_de_hough.png";
            frame.setTitle("Accumulateur");
            ImageIcon Imc = new ImageIcon("espace_de_hough.png");
            Image imag = Imc.getImage();
            JLabel label2 = new JLabel(Imc);
            frame.getContentPane().add(label2);
            frame.pack();
            frame.setSize(imag.getWidth(null), imag.getHeight(null));
            frame.setVisible(true);
            frame.isPreferredSizeSet();
            label2.repaint();
            frame.repaint();
        }
        if (source == annuler) {
            repeter.setEnabled(true);
            panel_1.figures1.add(panel_1.figures.getLast());
            panel_1.figures.removeLast();
            repaint();
            validate();
        }
        if (source == repeter) {
            if (panel_1.figures1.isEmpty()) {
                annuler.setEnabled(false);
            }
            panel_1.figures.add(panel_1.figures1.getLast());
            panel_1.figures1.removeLast();
            repaint();
            validate();
        }
        if (source == New || source == vider) {
            panel_1.setlwn(c);
            annuler.setEnabled(false);
            repeter.setEnabled(false);
            barre1.removeAll();
            barre1.add(vide1);
            barre1.add(vide2);
            barre1.repaint();
            info.setText("");
            panel_1.vider_dessin();
        }
        if (source == premier_p) {
            setCursor(JFrame.DEFAULT_CURSOR);
            panel_1.flag = "Premier_plan";
        }
        if (source == arriere_p) {
            setCursor(JFrame.DEFAULT_CURSOR);
            panel_1.flag = "Arriere_plan";
        }
        if (source == Ouvrir) {
            panel_1.setlwn(c);
            setCursor(JFrame.DEFAULT_CURSOR);
            JFileChooser chooser = new JFileChooser();
            chooser.setAccessory(new FilePreviewer(chooser));
            chooser.setCurrentDirectory(new File("."));
            FileFilter bmp = new filtre("Images BMP", ".bmp");
            FileFilter gif = new filtre("Image GIF", ".gif");
            FileFilter png = new filtre("Image PNG", ".png");
            FileFilter jpeg = new filtre("Images JPEG", ".jpg");
            chooser.addChoosableFileFilter(bmp);
            chooser.addChoosableFileFilter(gif);
            chooser.addChoosableFileFilter(png);
            chooser.addChoosableFileFilter(jpeg);
            chooser.setDialogTitle("Ouvrir une image");
            chooser.setMultiSelectionEnabled(false);
            int r = chooser.showOpenDialog(this);
            if (r == JFileChooser.APPROVE_OPTION) {
                file_image = chooser.getSelectedFile();
                nom_image = chooser.getSelectedFile().getName();
                path = file_image.getPath();
                panel_1.loadImage(file_image, nom_image);
                repaint();
                setTitle("Dessiner - " + nom_image);
            }
        }
    }
