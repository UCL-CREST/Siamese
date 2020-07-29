    public static void main(String[] args) throws Exception {
        Reader trainingFile = null;
        int restArgs = commandOptions.processOptions(args);
        if (restArgs != args.length) {
            commandOptions.printUsage(true);
            throw new IllegalArgumentException("Unexpected arg " + args[restArgs]);
        }
        if (trainFileOption.value == null) {
            commandOptions.printUsage(true);
            throw new IllegalArgumentException("Expected --train-file FILE");
        }
        if (modelFileOption.value == null) {
            commandOptions.printUsage(true);
            throw new IllegalArgumentException("Expected --model-file FILE");
        }
        ZipFile zipFile = new ZipFile(modelFileOption.value);
        ZipEntry zipEntry = zipFile.getEntry("crf-info.xml");
        CRFInfo crfInfo = new CRFInfo(zipFile.getInputStream(zipEntry));
        byte[] crfInfoBytes = new byte[(int) zipEntry.getSize()];
        zipFile.getInputStream(zipEntry).read(crfInfoBytes);
        CRF4 crf = createCRF(trainFileOption.value, crfInfo);
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(modelFileOption.value));
        zos.putNextEntry(new ZipEntry("crf-info.xml"));
        zos.write(crfInfoBytes);
        zos.closeEntry();
        zos.putNextEntry(new ZipEntry("crf-model.ser"));
        ObjectOutputStream oos = new ObjectOutputStream(zos);
        oos.writeObject(crf);
        oos.flush();
        zos.closeEntry();
        zos.close();
    }
