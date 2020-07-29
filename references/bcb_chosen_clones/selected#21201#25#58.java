    @Override
    public Void execute() throws CommandExecutionException {
        try {
            File outputFile = new File(file, "AmeMailCheckerSettings-" + System.currentTimeMillis() + ".zip");
            int BUFFER = 2048;
            BufferedInputStream origin;
            FileOutputStream destination = new FileOutputStream(outputFile);
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(destination));
            out.setLevel(ZipOutputStream.STORED);
            byte data[] = new byte[BUFFER];
            String[] paths = new String[3];
            Settings settings = ApplicationContext.getInstance().getSettings();
            paths[0] = settings.getUserDataFilePath();
            paths[1] = settings.getUserKeyFilePath();
            paths[2] = settings.getUserPreferencesFilePath();
            for (int i = 0; i < paths.length; i++) {
                File file = new File(paths[i]);
                FileInputStream fi = new FileInputStream(file);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(file.getName());
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            throw new CommandExecutionException(ExceptionCode.UNABLE_SAVE_FILE, e.getMessage(), e);
        }
        return null;
    }
