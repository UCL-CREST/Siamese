    private void zipFolder(String internalPath, File dirZip, ZipOutputStream zipOut) throws IOException {
        byte[] buf = new byte[4096];
        File[] fileArray = dirZip.listFiles();
        String fileName = "";
        if (fileArray != null) {
            for (int i = 0; i < fileArray.length; i++) {
                fileName = fileArray[i].getName();
                if (fileArray[i].isDirectory() && !fileName.startsWith(".")) {
                    zipFolder(internalPath + fileName + "/", fileArray[i], zipOut);
                } else if (!fileArray[i].isDirectory()) {
                    boolean include = true;
                    if (clientSession.getTranslationFileMapToSubModuleID().containsKey(fileName)) {
                        System.out.println("File: " + fileName);
                        String submoduleID = clientSession.getTranslationFileMapToSubModuleID().get(fileName);
                        System.out.println("SubModuleID: " + submoduleID);
                        String moduleID = null;
                        for (IModule module : taxonomy.getModules()) {
                            ISubModule sm = module.getSubModuleByID(submoduleID);
                            if (sm != null) {
                                moduleID = module.getID();
                                System.out.println("ModuelID: " + moduleID);
                            }
                        }
                        if (!clientSession.getClientSelection().constainsSubModule(moduleID, submoduleID)) {
                            System.out.println("NOT INCLUDED");
                            include = false;
                        }
                    }
                    if (include) {
                        FileInputStream inFile = new FileInputStream(fileArray[i]);
                        zipOut.putNextEntry(new ZipEntry(internalPath + fileName));
                        int len;
                        while ((len = inFile.read(buf)) > 0) {
                            zipOut.write(buf, 0, len);
                        }
                        inFile.close();
                    }
                }
            }
        }
    }
