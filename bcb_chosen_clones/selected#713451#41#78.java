    private void makeBackup() {
        try {
            FiltroFileDat filter = new FiltroFileDat("zip");
            JFileChooser fc = new JFileChooser();
            fc.setFileFilter(filter);
            fc.addChoosableFileFilter(filter);
            fc.showSaveDialog(parent);
            if (fc.getSelectedFile().exists()) {
                alert.avviso("File Esistente.");
                return;
            }
            zipFileName = new File(controlzipFile(fc.getSelectedFile().getAbsolutePath()));
        } catch (Exception exc) {
        }
        File[] listFiles = dir.listFiles();
        System.out.println(dir.getAbsolutePath());
        System.out.println(zipFileName.getAbsolutePath());
        int dirLength = listFiles.length;
        byte[] buffer = new byte[18024];
        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName.getAbsolutePath()));
            out.setLevel(Deflater.DEFAULT_COMPRESSION);
            for (int i = 0; i < dirLength; i++) {
                if (listFiles[i].isDirectory()) continue;
                FileInputStream in = new FileInputStream(listFiles[i].getAbsolutePath());
                out.putNextEntry(new ZipEntry(listFiles[i].getName()));
                int len;
                while ((len = in.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (Exception exc) {
            System.out.println("errore");
        }
    }
