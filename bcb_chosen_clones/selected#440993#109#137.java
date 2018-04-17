    public static boolean copiaSeguridad(File destino) {
        try {
            if (!destino.toString().endsWith(".ignotus")) {
                destino = new File(destino.toString() + ".ignotus");
            }
            FileOutputStream dest = new FileOutputStream(destino);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            File datos = new File(DATOS);
            for (File arch : datos.listFiles()) {
                if (arch.getName().endsWith(".lck")) {
                    continue;
                }
                ZipEntry entry = new ZipEntry(arch.getName());
                out.putNextEntry(entry);
                FileInputStream fi = new FileInputStream(arch);
                BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);
                int count;
                byte data[] = new byte[BUFFER];
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }
