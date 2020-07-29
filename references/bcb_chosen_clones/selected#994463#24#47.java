    public void notifyStartup(StartupEvent event) {
        logger.info("Running revision info...");
        Process pr = null;
        try {
            pr = Runtime.getRuntime().exec("svn info");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Running revision info...done.");
        logger.info("Saving revision info output...");
        try {
            BufferedReader procout = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            BufferedWriter fileout = new BufferedWriter(new FileWriter(Controler.getOutputFilename(this.filename)));
            String line;
            while ((line = procout.readLine()) != null) {
                fileout.write(line);
                fileout.write(System.getProperty("line.separator"));
            }
            fileout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("Saving revision info output...done.");
    }
