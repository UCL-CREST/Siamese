    private void zipDir(JTextArea out, String dir2zip, ZipOutputStream zos, String pathToDir) throws IOException {
        File zipDir = new File(dir2zip);
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(zipDir, dirList[i]);
            if (f.isDirectory()) {
                String filePath = f.getPath();
                zipDir(out, filePath, zos, pathToDir);
                continue;
            }
            out.append(Messages.getString("Zipping_file") + " " + f.getName() + "...\n");
            FileInputStream fis = new FileInputStream(f);
            ZipEntry anEntry = new ZipEntry(f.getPath().substring(pathToDir.length()));
            zos.putNextEntry(anEntry);
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            fis.close();
        }
    }
