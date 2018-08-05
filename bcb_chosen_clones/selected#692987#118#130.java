    private boolean CrearDirectorioJugador(String nombre) {
        File directory = new File("Save/" + nombre);
        if (directory.exists()) {
            int value = PopUp.YES_OPTION;
            if (value == PopUp.YES_OPTION) {
                File files[] = directory.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) deleteDirectory(files[i]); else files[i].delete();
                }
            } else return false;
        } else directory.mkdir();
        return true;
    }
