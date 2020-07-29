    public void copyDirectory(File sourceLocation, File targetLocation) throws IOException {
        System.out.println(sourceLocation.exists());
        System.out.println(sourceLocation.list());
        System.out.println("Provo a copiare...");
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                System.out.println("Creo la cartella di destinazione perch� non esiste...");
                targetLocation.mkdir();
            }
            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                System.out.println("Copio...");
                copyDirectory(new File(sourceLocation, children[i]), new File(targetLocation, children[i]));
            }
        } else {
            InputStream in = new FileInputStream(sourceLocation);
            OutputStream out = new FileOutputStream(targetLocation);
            System.out.println(" Copy the bits from instream to outstream");
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        }
    }
