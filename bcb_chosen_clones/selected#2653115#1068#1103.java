    public static void ExtraeArchivoJAR(String Archivo, String DirJAR, String DirDestino) {
        FileInputStream entrada = null;
        FileOutputStream salida = null;
        try {
            File f = new File(DirDestino + separador + Archivo);
            try {
                f.createNewFile();
            } catch (Exception sad) {
                sad.printStackTrace();
            }
            InputStream source = OP_Proced.class.getResourceAsStream(DirJAR + "/" + Archivo);
            BufferedInputStream in = new BufferedInputStream(source);
            FileOutputStream out = new FileOutputStream(f);
            int ch;
            while ((ch = in.read()) != -1) out.write(ch);
            in.close();
            out.close();
        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            if (entrada != null) {
                try {
                    entrada.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (salida != null) {
                try {
                    salida.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
