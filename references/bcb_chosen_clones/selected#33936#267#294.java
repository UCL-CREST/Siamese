                public void mouseClicked(java.awt.event.MouseEvent e) {
                    if (e.getClickCount() >= 2) {
                        int index = tabla.getSelectedRow();
                        index = tabla.convertRowIndexToModel(index);
                        Pdf p = listado.getPdf(index);
                        File f = new File(clienteActual.getIdCliente() + clienteActual.getNomSinEspacios() + clienteActual.getApelSinEspacios() + "/" + p.getName());
                        if (System.getProperty("os.name").equals("Linux") && !Desktop.isDesktopSupported()) {
                            Runtime obj = Runtime.getRuntime();
                            try {
                                obj.exec("okular " + f.toString());
                                cargarPdf();
                            } catch (IOException e1) {
                                JOptionPane.showMessageDialog(null, e1.getMessage());
                            }
                        } else {
                            try {
                                Desktop.getDesktop().open(f);
                                cargarPdf();
                            } catch (IOException e1) {
                                JOptionPane.showMessageDialog(null, e1.getMessage());
                            }
                        }
                    } else {
                        int index = tabla.getSelectedRow();
                        index = tabla.convertRowIndexToModel(index);
                        previsualizarPdf(index);
                    }
                }
