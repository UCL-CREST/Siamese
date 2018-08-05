    public void upgradeSingleFileModelToDirectory(File modelFile) throws IOException {
        byte[] buf = new byte[8192];
        int bytesRead = 0;
        File backupModelFile = new File(modelFile.getPath() + ".bak");
        FileInputStream oldModelIn = new FileInputStream(modelFile);
        FileOutputStream backupModelOut = new FileOutputStream(backupModelFile);
        while ((bytesRead = oldModelIn.read(buf)) >= 0) {
            backupModelOut.write(buf, 0, bytesRead);
        }
        backupModelOut.close();
        oldModelIn.close();
        buf = null;
        modelFile.delete();
        modelFile.mkdir();
        BufferedReader oldModelsBuff = new BomStrippingInputStreamReader(new FileInputStream(backupModelFile), "UTF-8");
        File metaDataFile = new File(modelFile, ConstantParameters.FILENAMEOFModelMetaData);
        BufferedWriter metaDataBuff = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(metaDataFile), "UTF-8"));
        for (int i = 0; i < 8; i++) {
            metaDataBuff.write(oldModelsBuff.readLine());
            metaDataBuff.write('\n');
        }
        metaDataBuff.close();
        int classIndex = 1;
        BufferedWriter modelWriter = null;
        String line = null;
        while ((line = oldModelsBuff.readLine()) != null) {
            if (line.startsWith("Class=") && line.contains("numTraining=") && line.contains("numPos=")) {
                if (modelWriter != null) {
                    modelWriter.close();
                }
                File nextModel = new File(modelFile, String.format(ConstantParameters.FILENAMEOFPerClassModel, Integer.valueOf(classIndex++)));
                modelWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(nextModel), "UTF-8"));
            }
            modelWriter.write(line);
            modelWriter.write('\n');
        }
        if (modelWriter != null) {
            modelWriter.close();
        }
    }
