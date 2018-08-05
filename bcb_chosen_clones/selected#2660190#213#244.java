    private void zipGeneratedFiles(String path, String nomeArquivo) {
        try {
            int BUFFER = 2048;
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(path + File.separatorChar + "mapas" + File.separatorChar + nomeArquivo + ".zip");
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            out.setLevel(ZipOutputStream.DEFLATED);
            byte data[] = new byte[BUFFER];
            String rootPath = File.listRoots()[0].getAbsolutePath();
            String files[] = new String[3];
            files[0] = rootPath + "tmp" + File.separatorChar + nomeArquivo + ".shp";
            files[1] = rootPath + "tmp" + File.separatorChar + nomeArquivo + ".shx";
            files[2] = rootPath + "tmp" + File.separatorChar + nomeArquivo + ".dbf";
            for (int i = 0; i < files.length; i++) {
                System.out.println("Adding: " + files[i]);
                FileInputStream fi = new FileInputStream(files[i]);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(files[i]);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
                File file = new File(files[i]);
                file.delete();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
