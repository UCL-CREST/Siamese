    public boolean chequearMarca(int a, int m, int d) {
        boolean existe = false;
        try {
            cantidadArchivos = obtenerCantidad() + 1;
            String filenametxt = "";
            String filenamezip = "";
            int dia = 0;
            int mes = 0;
            int ano = 0;
            for (int i = 1; i < cantidadArchivos; i++) {
                filenamezip = "recordatorio" + i + ".zip";
                filenametxt = "recordatorio" + i + ".txt";
                BufferedOutputStream dest = null;
                BufferedInputStream is = null;
                ZipEntry entry;
                ZipFile zipfile = new ZipFile(filenamezip);
                Enumeration e = zipfile.entries();
                while (e.hasMoreElements()) {
                    entry = (ZipEntry) e.nextElement();
                    is = new BufferedInputStream(zipfile.getInputStream(entry));
                    int count;
                    byte data[] = new byte[buffer];
                    FileOutputStream fos = new FileOutputStream(entry.getName());
                    dest = new BufferedOutputStream(fos, buffer);
                    while ((count = is.read(data, 0, buffer)) != -1) dest.write(data, 0, count);
                    dest.flush();
                    dest.close();
                    is.close();
                }
                DataInputStream input = new DataInputStream(new FileInputStream(filenametxt));
                dia = Integer.parseInt(input.readLine());
                mes = Integer.parseInt(input.readLine());
                ano = Integer.parseInt(input.readLine());
                if (ano == a && mes == m && dia == d) existe = true;
                input.close();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en: " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return (existe);
    }
