    public static void copyZip() {
        InputStream is;
        OutputStream os;
        String javacZip = "";
        try {
            if ("windows".equalsIgnoreCase(Compilador.getSo())) {
                javacZip = "javacWin.zip";
                is = UnZip.class.getResourceAsStream("javacWin.zip");
            } else if ("linux".equalsIgnoreCase(Compilador.getSo())) {
                javacZip = "javacLinux.zip";
                is = UnZip.class.getResourceAsStream("javacLinux.zip");
            }
            is = UnZip.class.getResourceAsStream(javacZip);
            File tempZip = File.createTempFile("tempJavacJTraductor", ".zip");
            tempZip.mkdir();
            tempZip.deleteOnExit();
            os = FileUtils.openOutputStream(tempZip);
            IOUtils.copy(is, os);
            is.close();
            os.close();
            extractZip(tempZip.getPath());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(PseutemView.mainPanel, "Error al copiar los archivos temporales necesarios para ejecutar el programa:\n\n" + ex, "Error copiando.", JOptionPane.ERROR_MESSAGE);
        }
    }
