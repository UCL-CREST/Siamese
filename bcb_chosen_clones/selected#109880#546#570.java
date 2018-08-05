    public void guardarCantidad() {
        try {
            String can = String.valueOf(cantidadArchivos);
            File archivo = new File("cantidadArchivos.txt");
            FileWriter fw = new FileWriter(archivo);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter salida = new PrintWriter(bw);
            salida.print(can);
            salida.close();
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream("cantidadArchivos.zip");
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            byte data[] = new byte[buffer];
            File f = new File("cantidadArchivos.txt");
            FileInputStream fi = new FileInputStream(f);
            origin = new BufferedInputStream(fi, buffer);
            ZipEntry entry = new ZipEntry("cantidadArchivos.txt");
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, buffer)) != -1) out.write(data, 0, count);
            out.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error en: " + e.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
