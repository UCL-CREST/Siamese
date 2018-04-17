    public void actionPerformed(ActionEvent e) {
        if (SAVEPICTURE.equals(e.getActionCommand())) {
            byte[] data = ((PicturePane) picturesPane.getSelectedComponent()).getImage();
            if (data == null) {
                return;
            }
            File f = Util.getFile(this, "Save file", true);
            if (f != null) {
                try {
                    Files.writeFile(f, data);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else if (SAVEDL.equals(e.getActionCommand())) {
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
                            String entryName = Hex.shortToHexString(fid) + ".bin";
                            InputStream dg = dl.getInputStream(fid);
                            zipOut.putNextEntry(new ZipEntry(entryName));
                            int bytesRead;
                            byte[] dgBytes = new byte[1024];
                            while ((bytesRead = dg.read(dgBytes)) > 0) {
                                zipOut.write(dgBytes, 0, bytesRead);
                            }
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
        } else if (VIEWDOCCERT.equals(e.getActionCommand())) {
            try {
                X509Certificate c = sodFile.getDocSigningCertificate();
                viewData(c.toString(), c.getEncoded());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (VIEWAAKEY.equals(e.getActionCommand())) {
            PublicKey k = dg13file.getPublicKey();
            viewData(k.toString(), k.getEncoded());
        } else if (VIEWEAPKEYS.equals(e.getActionCommand())) {
            String s = "";
            int count = 0;
            Set<Integer> ids = dg14file.getIds();
            List<byte[]> keys = new ArrayList<byte[]>();
            for (Integer id : ids) {
                if (count != 0) {
                    s += "\n";
                }
                if (id != -1) {
                    s += "Key identifier: " + id + "\n";
                }
                PublicKey k = dg14file.getKey(id);
                s += k.toString();
                keys.add(k.getEncoded());
                count++;
            }
            viewData(s, keys);
        }
    }
