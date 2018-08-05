    private void startExportSolverTask(Solver s, File f, Tasks task) throws FileNotFoundException, SQLException, IOException {
        task.setOperationName("Exporting solver binaries...");
        FileOutputStream fos;
        if (f.isDirectory()) {
            fos = new FileOutputStream(f.getAbsolutePath() + System.getProperty("file.separator") + s.getName() + ".zip");
        } else {
            fos = new FileOutputStream(f);
        }
        ZipOutputStream zos = new ZipOutputStream(fos);
        Vector<SolverBinaries> bins = s.getSolverBinaries();
        for (int i = 0; i < bins.size(); i++) {
            task.setTaskProgress((float) (i + 1) / (float) bins.size());
            SolverBinaries b = bins.get(i);
            InputStream binStream = SolverBinariesDAO.getZippedBinaryFile(b);
            ZipInputStream zis = new ZipInputStream(binStream);
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                ZipEntry newEntry = new ZipEntry(b.getBinaryName() + "_" + b.getVersion() + "/" + entry.getName());
                zos.putNextEntry(newEntry);
                for (int c = zis.read(); c != -1; c = zis.read()) {
                    zos.write(c);
                }
                zos.closeEntry();
                zis.closeEntry();
            }
            zis.close();
            binStream.close();
        }
        zos.close();
        fos.close();
    }
