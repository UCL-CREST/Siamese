        public void ejecutarSeleccionado() {
            String ruta = GestorCompartidos.getInstancia().getGestorDisco().getDirectorioCompletos() + "/" + _lblNombre.getText();
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.BROWSE)) {
                        desktop.browse(new URI(ruta));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
