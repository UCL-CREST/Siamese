    private void exportLearningUnitView(File selectedFile) {
        if (learningUnitViewElementsManager.isOriginalElementsOnly()) {
            String[] elementIds = learningUnitViewElementsManager.getAllLearningUnitViewElementIds();
            for (int i = 0; i < elementIds.length; i++) {
                FSLLearningUnitViewElement element = learningUnitViewElementsManager.getLearningUnitViewElement(elementIds[i], false);
                if (element.getLastModificationDate() == null) {
                    element.setLastModificationDate(String.valueOf(new Date().getTime()));
                    element.setModified(true);
                }
            }
            learningUnitViewElementsManager.setModified(true);
            learningUnitViewManager.saveLearningUnitViewData();
            if (selectedFile != null) {
                try {
                    File outputFile = selectedFile;
                    String fileName = outputFile.getName();
                    StringBuffer extension = new StringBuffer();
                    if (fileName.length() >= 5) {
                        for (int i = 5; i > 0; i--) {
                            extension.append(fileName.charAt(fileName.length() - i));
                        }
                    }
                    if (!extension.toString().equals(".fslv")) {
                        outputFile.renameTo(new File(outputFile.getAbsolutePath() + ".fslv"));
                        outputFile = new File(outputFile.getAbsolutePath() + ".fslv");
                    }
                    File files[] = selectedFile.getParentFile().listFiles();
                    int returnValue = FLGOptionPane.OK_OPTION;
                    for (int i = 0; i < files.length; i++) {
                        if (outputFile.getAbsolutePath().equals(files[i].getAbsolutePath())) {
                            returnValue = FLGOptionPane.showConfirmDialog(internationalization.getString("dialog.exportLearningUnitView.fileExits.message"), internationalization.getString("dialog.exportLearningUnitView.fileExits.title"), FLGOptionPane.OK_CANCEL_OPTION, FLGOptionPane.QUESTION_MESSAGE);
                            break;
                        }
                    }
                    if (returnValue == FLGOptionPane.OK_OPTION) {
                        FileOutputStream os = new FileOutputStream(outputFile);
                        ZipOutputStream zipOutputStream = new ZipOutputStream(os);
                        ZipEntry zipEntry = new ZipEntry("dummy");
                        zipOutputStream.putNextEntry(zipEntry);
                        zipOutputStream.closeEntry();
                        zipOutputStream.flush();
                        zipOutputStream.finish();
                        zipOutputStream.close();
                        final File outFile = outputFile;
                        (new Thread() {

                            public void run() {
                                try {
                                    ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(outFile));
                                    File[] files = (new File(learningUnitViewManager.getLearningUnitViewOriginalDataDirectory().getPath())).listFiles();
                                    int maxSteps = files.length;
                                    int step = 1;
                                    exportProgressDialog = new FLGImageProgressDialog(null, 0, maxSteps, 0, getClass().getClassLoader().getResource("freestyleLearning/homeCore/images/fsl.gif"), (Color) UIManager.get("FSLColorBlue"), (Color) UIManager.get("FSLColorRed"), internationalization.getString("learningUnitViewExport.rogressbarText"));
                                    exportProgressDialog.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                                    exportProgressDialog.setBarValue(step);
                                    buildExportZipFile("", zipOutputStream, files, step);
                                    zipOutputStream.flush();
                                    zipOutputStream.finish();
                                    zipOutputStream.close();
                                    exportProgressDialog.setBarValue(maxSteps);
                                    exportProgressDialog.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                                    exportProgressDialog.dispose();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        os.close();
                    }
                } catch (Exception exp) {
                    exp.printStackTrace();
                }
            }
        } else {
        }
    }
