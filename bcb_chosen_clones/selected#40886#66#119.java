    public void writeGerberFiles() {
        boolean zipped = false;
        File f;
        File subDir;
        if (camOutput.getZipped() == CamOutput.Zipped.ZIPPED) {
            zipped = true;
        }
        try {
            if (zipped) {
                f = file;
                f.createNewFile();
                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(f));
                zos.setMethod(ZipOutputStream.DEFLATED);
                zos.setLevel(Deflater.DEFAULT_COMPRESSION);
                OutputStreamWriter osw;
                osw = new OutputStreamWriter(zos);
                for (CamOutputTableItem coti : camOutput.getCamFiles()) {
                    if (coti instanceof CamFile) {
                        CamFile cf = (CamFile) coti;
                        zos.putNextEntry(new ZipEntry(cf.getFileName()));
                        cf.writeCamFile(osw);
                        osw.flush();
                        zos.closeEntry();
                        zos.closeEntry();
                    } else if (coti instanceof ReadmeFile) {
                        ReadmeFile rf = (ReadmeFile) coti;
                        zos.putNextEntry(new ZipEntry(rf.getFileName()));
                        osw.write(rf.getText());
                        osw.flush();
                        zos.closeEntry();
                    }
                }
                zos.close();
            } else {
                subDir = file;
                subDir.mkdir();
                for (CamOutputTableItem coti : camOutput.getCamFiles()) {
                    f = new File(subDir, coti.getFileName());
                    f.createNewFile();
                    Writer w = new FileWriter(f);
                    if (coti instanceof CamFile) {
                        CamFile cf = (CamFile) coti;
                        cf.writeCamFile(w);
                    } else if (coti instanceof ReadmeFile) {
                        ReadmeFile rf = (ReadmeFile) coti;
                        w.write(rf.getText());
                    }
                    w.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
