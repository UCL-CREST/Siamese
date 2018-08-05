    private String comprimirPonencias(String[] ponencias) throws IOException {
        BufferedInputStream origin = null;
        Random random = new Random();
        String shortFileName = "ponencias_" + random.nextInt(10000) + ".zip";
        String zipRelativeName = showZipPath + shortFileName;
        String zipName = getServlet().getServletContext().getRealPath(zipRelativeName);
        FileOutputStream dest = new FileOutputStream(zipName);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        byte[] data = new byte[BUFFER_SIZE];
        for (int i = 0; i < ponencias.length; i++) {
            String fileName = filePath + ponencias[i] + ".pdf";
            FileInputStream fi = new FileInputStream(fileName);
            origin = new BufferedInputStream(fi, BUFFER_SIZE);
            ZipEntry entry = new ZipEntry(ponencias[i] + ".pdf");
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
        }
        out.close();
        return shortFileName;
    }
