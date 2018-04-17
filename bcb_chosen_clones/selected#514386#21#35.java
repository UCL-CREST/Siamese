    public static void open(byte[] data, String extension) {
        if (data != null && Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            File file;
            try {
                file = File.createTempFile("tmp", "." + extension);
                file.deleteOnExit();
                FileUtils.writeByteArrayToFile(file, data);
                desktop.open(file);
            } catch (IOException e) {
                String message = "No ha sido posible abrir el fichero";
                JOptionPane.showMessageDialog(null, message, "Error de datos", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
