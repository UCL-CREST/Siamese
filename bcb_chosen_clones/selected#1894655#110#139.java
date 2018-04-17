    public boolean restore(File directory) {
        log.debug("restore file from directory " + directory.getAbsolutePath());
        try {
            if (!directory.exists()) return false;
            String[] operationFileNames = directory.list();
            if (operationFileNames.length < 6) {
                log.error("Only " + operationFileNames.length + " files found in directory " + directory.getAbsolutePath());
                return false;
            }
            int fileCount = 0;
            for (int i = 0; i < operationFileNames.length; i++) {
                if (!operationFileNames[i].toUpperCase().endsWith(".XML")) continue;
                log.debug("found file: " + operationFileNames[i]);
                fileCount++;
                File filein = new File(directory.getAbsolutePath() + File.separator + operationFileNames[i]);
                File fileout = new File(operationsDirectory + File.separator + operationFileNames[i]);
                FileReader in = new FileReader(filein);
                FileWriter out = new FileWriter(fileout);
                int c;
                while ((c = in.read()) != -1) out.write(c);
                in.close();
                out.close();
            }
            if (fileCount < 6) return false;
            return true;
        } catch (Exception e) {
            log.error("Exception while restoring operations files, may not be complete: " + e);
            return false;
        }
    }
