    public void actionPerformed(ActionEvent e) {
        this.lblStatusBar.setText("");
        this.lblStatusBar.setForeground(Color.black);
        if (e.getSource().equals(archivoNuevo)) {
            int n = JOptionPane.showConfirmDialog(this, "�Desea guardar los cambios?", "Alerta", JOptionPane.YES_NO_CANCEL_OPTION);
            if (n == JOptionPane.YES_OPTION) {
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showSaveDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    String path = file.getAbsolutePath();
                    try {
                        Grafo.guardaInformacion(path);
                        this.lblStatusBar.setText("Archivo Guardado: " + path);
                    } catch (Exception ex) {
                        this.lblStatusBar.setText("No fue posible guardar el archivo");
                    }
                }
            } else if (n == JOptionPane.CANCEL_OPTION) {
                return;
            }
            Main.getAgente().clear();
        } else if (e.getSource().equals(archivoAbrir)) {
            if (Persona.getPersonas().length > 0) {
                int n = JOptionPane.showConfirmDialog(this, "�Desea guardar los cambios?", "Alerta", JOptionPane.YES_NO_CANCEL_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    JFileChooser fc = new JFileChooser();
                    int returnVal = fc.showSaveDialog(this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        String path = file.getAbsolutePath();
                        try {
                            Grafo.guardaInformacion(path);
                            this.lblStatusBar.setText("Archivo Guardado: " + path);
                        } catch (Exception ex) {
                            this.lblStatusBar.setText("No fue posible guardar el archivo");
                        }
                    }
                } else if (n == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            Main.getAgente().clear();
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String path = file.getAbsolutePath();
                try {
                    Grafo.cargaGrafo(path);
                    this.lblStatusBar.setText("Red Cargada: " + path);
                } catch (Exception ex) {
                    this.lblStatusBar.setText("No fue posible abrir el archivo");
                }
            }
        } else if (e.getSource().equals(archivoGuardar)) {
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                String path = file.getAbsolutePath();
                try {
                    Grafo.guardaInformacion(path);
                    this.lblStatusBar.setText("Archivo Guardado: " + path);
                } catch (Exception ex) {
                    this.lblStatusBar.setText("No fue posible guardar el archivo");
                }
            }
        } else if (e.getSource().equals(archivoCerrar)) {
            System.exit(0);
        } else if (e.getSource().equals(simulacionAtributos)) {
            manejadorAtributos ma = new manejadorAtributos();
            ma.setVisible(true);
        } else if (e.getSource().equals(search)) {
            if (this.seleccionada == null) {
                this.lblStatusBar.setText("Seleccione una persona sobre la cual realizar la busqueda");
                this.lblStatusBar.setForeground(Color.red);
            } else {
                this.lblStatusBar.setForeground(Color.black);
                this.lblStatusBar.setText("Buscando amigos...");
                Persona sel = this.seleccionada;
                int nivel = Integer.parseInt(this.niveles.getText());
                int error_range = Integer.parseInt(this.niveles.getText());
                if (nivel > 0 && error_range > 0) {
                    LinkedList<Persona> results = Main.getAgente().buscarAmigos(this.seleccionada, nivel, error_range);
                    this.seleccionada = sel;
                    this.updatePropertiesPanel();
                    this.lblStatusBar.setText("Busqueda Finalizada: " + results.size() + " Personas encontradas.");
                    if (results.size() == 0) {
                        JOptionPane.showMessageDialog(this, "No se encontraron personas", "Resultados", JOptionPane.ERROR_MESSAGE);
                    } else {
                        String cols[] = { "Persona" };
                        Object data[][] = new Object[results.size()][1];
                        int i = 0;
                        for (Persona p : results) {
                            data[i][0] = p.getNombre();
                            i++;
                        }
                        pnlResultados.removeAll();
                        pnlResultados.add(new JTable(data, cols));
                        pnlResultados.repaint();
                        DisplayAttributesFrame.refreshWindows();
                    }
                } else if (nivel <= 0) {
                    this.lblStatusBar.setText("El nivel debe de ser mayor a 0");
                    this.lblStatusBar.setForeground(Color.red);
                } else if (error_range <= 0) {
                    this.lblStatusBar.setText("El nivel de compatibilidad debe de ser mayor a 0");
                    this.lblStatusBar.setForeground(Color.red);
                }
            }
        } else if (e.getSource().equals(simulacionAgregar)) {
            AgregaPersonaFrame apf = new AgregaPersonaFrame();
            apf.setVisible(true);
        } else if (e.getSource().equals(blacklist)) {
            if (this.seleccionada == null) {
                this.lblStatusBar.setText("Seleccione una persona sobre la cual realizar la busqueda");
                this.lblStatusBar.setForeground(Color.red);
            } else {
                Persona x = this.seleccionada;
                System.out.println(x.toString());
                BlackListFrame blf = new BlackListFrame(x);
                blf.setVisible(true);
            }
        } else if (e.getSource().equals(ayudaInfo)) {
            about a = new about();
            a.setVisible(true);
        } else if (e.getSource().equals(simulacionBorrar)) {
            if (seleccionada != null) {
                Main.getAgente().quitarPersona(seleccionada);
                for (DisplayAttributesFrame ve : DisplayAttributesFrame.ventanas) {
                    if (ve.p.equals(seleccionada)) {
                        ve.dispose();
                    }
                }
                this.removeElement(seleccionada);
                this.amigos.repaint();
                this.seleccionada = null;
                this.updatePropertiesPanel();
            }
        } else if (e.getSource().equals(edit)) {
            if (this.seleccionada == null) {
                this.lblStatusBar.setText("Seleccione una persona sobre la cual realizar la busqueda");
                this.lblStatusBar.setForeground(Color.red);
            } else {
                Persona y = this.seleccionada;
                AgregaPersonaFrame x = new AgregaPersonaFrame(y);
                x.setVisible(true);
            }
        }
    }
