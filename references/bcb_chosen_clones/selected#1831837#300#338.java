    private JButton getButtonImagen() {
        if (buttonImagen == null) {
            buttonImagen = new JButton();
            buttonImagen.setText("Cargar Imagen");
            buttonImagen.setIcon(new ImageIcon(getClass().getResource("/data/icons/view_sidetree.png")));
            buttonImagen.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JFileChooser fc = new JFileChooser();
                    fc.addChoosableFileFilter(new ImageFilter());
                    fc.setFileView(new ImageFileView());
                    fc.setAccessory(new ImagePreview(fc));
                    int returnVal = fc.showDialog(Resorces.this, "Seleccione una imagen");
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        String rutaGlobal = System.getProperty("user.dir") + file.separator + "data" + file.separator + "imagenes" + file.separator + file.getName();
                        String rutaRelativa = "data" + file.separator + "imagenes" + file.separator + file.getName();
                        try {
                            FileInputStream fis = new FileInputStream(file);
                            FileOutputStream fos = new FileOutputStream(rutaGlobal, true);
                            FileChannel canalFuente = fis.getChannel();
                            FileChannel canalDestino = fos.getChannel();
                            canalFuente.transferTo(0, canalFuente.size(), canalDestino);
                            fis.close();
                            fos.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        imagen.setImagenURL(rutaRelativa);
                        System.out.println(rutaGlobal + " " + rutaRelativa);
                        buttonImagen.setIcon(new ImageIcon(getClass().getResource("/data/icons/view_sidetreeOK.png")));
                        labelImagenPreview.setIcon(gui.procesadorDatos.escalaImageIcon(imagen.getImagenURL()));
                    } else {
                    }
                }
            });
        }
        return buttonImagen;
    }
