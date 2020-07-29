    private void saveDrivingLicense() {
        collectDrivingLicenseData();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(net.sourceforge.scuba.util.Files.ZIP_FILE_FILTER);
        int choice = fileChooser.showSaveDialog(getContentPane());
        switch(choice) {
            case JFileChooser.APPROVE_OPTION:
                try {
                    File file = fileChooser.getSelectedFile();
                    FileOutputStream fileOut = new FileOutputStream(file);
                    ZipOutputStream zipOut = new ZipOutputStream(fileOut);
                    for (short fid : dl.getFileList()) {
                        String eap = "";
                        if (fid == DrivingLicenseService.EF_DG2 && eapDG2.isEnabled() && eapDG2.isSelected()) {
                            eap = "eap";
                        }
                        if (fid == DrivingLicenseService.EF_DG3 && eapDG3.isEnabled() && eapDG3.isSelected()) {
                            eap = "eap";
                        }
                        String entryName = Hex.shortToHexString(fid) + eap + ".bin";
                        InputStream dg = dl.getInputStream(fid);
                        zipOut.putNextEntry(new ZipEntry(entryName));
                        int bytesRead;
                        byte[] dgBytes = new byte[1024];
                        while ((bytesRead = dg.read(dgBytes)) > 0) {
                            zipOut.write(dgBytes, 0, bytesRead);
                        }
                        zipOut.closeEntry();
                    }
                    byte[] keySeed = dl.getKeySeed();
                    if (keySeed != null) {
                        String entryName = "keyseed.bin";
                        zipOut.putNextEntry(new ZipEntry(entryName));
                        zipOut.write(keySeed);
                        zipOut.closeEntry();
                    }
                    PrivateKey aaPrivateKey = dl.getAAPrivateKey();
                    if (aaPrivateKey != null) {
                        String entryName = "aaprivatekey.der";
                        zipOut.putNextEntry(new ZipEntry(entryName));
                        zipOut.write(aaPrivateKey.getEncoded());
                        zipOut.closeEntry();
                    }
                    PrivateKey caPrivateKey = dl.getEAPPrivateKey();
                    if (caPrivateKey != null) {
                        String entryName = "caprivatekey.der";
                        zipOut.putNextEntry(new ZipEntry(entryName));
                        zipOut.write(caPrivateKey.getEncoded());
                        zipOut.closeEntry();
                    }
                    CVCertificate cvCert = dl.getCVCertificate();
                    if (cvCert != null) {
                        String entryName = "cacert.cvcert";
                        zipOut.putNextEntry(new ZipEntry(entryName));
                        zipOut.write(cvCert.getDEREncoded());
                        zipOut.closeEntry();
                    }
                    zipOut.finish();
                    zipOut.close();
                    fileOut.flush();
                    fileOut.close();
                    break;
                } catch (IOException fnfe) {
                    fnfe.printStackTrace();
                }
            default:
                break;
        }
    }
