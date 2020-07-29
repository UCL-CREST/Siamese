    private void createJAR(String currentPath, String fullName, ArrayList<File> files) {
        try {
            FileOutputStream fos = new FileOutputStream(fullName);
            JarOutputStream jos = new JarOutputStream(fos);
            for (int i = 0; i < files.size(); i++) {
                myProcess.currentFile = currentPath + files.get(i);
                myProcess.currentProgress = 0;
                myProcess.totalProgress = ((i * 100) / files.size());
                final long length = new File(myProcess.currentFile).length();
                FileInputStream fis = new FileInputStream(myProcess.currentFile);
                jos.putNextEntry(new ZipEntry(myProcess.currentFile));
                byte[] buf = new byte[1024];
                int len = 0;
                int total = 0;
                while ((len = fis.read(buf)) > 0) {
                    total += len;
                    jos.write(buf, 0, len);
                    myProcess.currentProgress = (int) (total * 100 / length);
                    if (myProcess.cancel) {
                        logger.info("Cancel packing.");
                        fis.close();
                        jos.close();
                        fos.close();
                        return;
                    }
                }
                jos.closeEntry();
                fis.close();
            }
            jos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        myProcess.totalProgress = 100;
        MainGUI.app.getSource().refreshFiles();
    }
