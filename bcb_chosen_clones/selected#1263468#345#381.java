    private JButton getButtonSonido() {
        if (buttonSonido == null) {
            buttonSonido = new JButton();
            buttonSonido.setText(Messages.getString("gui.AdministracionResorces.15"));
            buttonSonido.setIcon(new ImageIcon(getClass().getResource("/es/unizar/cps/tecnoDiscap/data/icons/view_sidetree.png")));
            buttonSonido.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    JFileChooser fc = new JFileChooser();
                    fc.addChoosableFileFilter(new SoundFilter());
                    int returnVal = fc.showDialog(AdministracionResorces.this, Messages.getString("gui.AdministracionResorces.17"));
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        String rutaGlobal = System.getProperty("user.dir") + "/" + rutaDatos + "sonidos/" + file.getName();
                        String rutaRelativa = rutaDatos + "sonidos/" + file.getName();
                        try {
                            FileInputStream fis = new FileInputStream(file);
                            FileOutputStream fos = new FileOutputStream(rutaGlobal, true);
                            FileChannel canalFuente = fis.getChannel();
                            FileChannel canalDestino = fos.getChannel();
                            canalFuente.transferTo(0, canalFuente.size(), canalDestino);
                            fis.close();
                            fos.close();
                            imagen.setSonidoURL(rutaRelativa);
                            System.out.println(rutaGlobal + " " + rutaRelativa);
                            buttonSonido.setIcon(new ImageIcon(getClass().getResource("/es/unizar/cps/tecnoDiscap/data/icons/view_sidetreeOK.png")));
                            gui.getAudio().reproduceAudio(imagen);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                    }
                }
            });
        }
        return buttonSonido;
    }
