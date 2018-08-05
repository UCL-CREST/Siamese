    @Override
    public void doExecute(String[] args) {
        if (args.length != 2) {
            printUsage();
        } else {
            int fileNo = 0;
            try {
                fileNo = Integer.parseInt(args[1]) - 1;
            } catch (NumberFormatException e) {
                printUsage();
                return;
            }
            if (fileNo < 0) {
                printUsage();
                return;
            }
            StorageFile[] files = (StorageFile[]) ctx.getRemoteDir().listFiles();
            try {
                StorageFile file = files[fileNo];
                File outFile = getOutFile(file);
                FileOutputStream out = new FileOutputStream(outFile);
                InputStream in = file.openStream();
                IOUtils.copy(in, out);
                IOUtils.closeQuietly(out);
                afterSave(outFile);
                if (outFile.exists()) {
                    print("File written to: " + outFile.getAbsolutePath());
                }
            } catch (IOException e) {
                printError("Failed to load file. " + e.getMessage());
            } catch (Exception e) {
                printUsage();
                return;
            }
        }
    }
