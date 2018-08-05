    public Boolean execute(String sDestinationFilename) {
        ResourcesManager manager = new ResourcesManager();
        BufferedInputStream oOrigin = null;
        FileOutputStream oDestination;
        ZipOutputStream oOutput;
        byte[] aData;
        try {
            System.out.println("Compressing the model");
            oDestination = new FileOutputStream(sDestinationFilename);
            oOutput = new ZipOutputStream(new BufferedOutputStream(oDestination));
            Collection<File> files = manager.getFiles(tempFilePath);
            aData = new byte[BUFFER_SIZE];
            Iterator<File> fileIterator = files.iterator();
            while (fileIterator.hasNext()) {
                File currentFile = fileIterator.next();
                String sFilename = currentFile.getAbsolutePath();
                sFilename = LibraryString.replaceAll(sFilename, Strings.BAR135, Strings.BAR45);
                FileInputStream fisInput = new FileInputStream(sFilename);
                oOrigin = new BufferedInputStream(fisInput, BUFFER_SIZE);
                sFilename = sFilename.substring(sFilename.indexOf(tempFilePath) + tempFilePath.length() + 1);
                ZipEntry oEntry = new ZipEntry(sFilename.replace('\\', '/'));
                oOutput.putNextEntry(oEntry);
                int iCount;
                while ((iCount = oOrigin.read(aData, 0, BUFFER_SIZE)) != -1) {
                    oOutput.write(aData, 0, iCount);
                }
                oOrigin.close();
                fisInput.close();
            }
            oOutput.close();
            System.out.println("Model compressed");
            AgentFilesystem.removeDir(tempFilePath);
        } catch (Exception oException) {
            oException.printStackTrace();
            return false;
        }
        return true;
    }
