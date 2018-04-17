    public static void copyFile(String fromPath, String toPath) {
        try {
            File inputFile = new File(fromPath);
            String dirImg = (new File(toPath)).getParent();
            File tmp = new File(dirImg);
            if (!tmp.exists()) {
                tmp.mkdir();
            }
            File outputFile = new File(toPath);
            if (!inputFile.getCanonicalPath().equals(outputFile.getCanonicalPath())) {
                FileInputStream in = new FileInputStream(inputFile);
                FileOutputStream out = new FileOutputStream(outputFile);
                int c;
                while ((c = in.read()) != -1) out.write(c);
                in.close();
                out.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            LogHandler.log(ex.getMessage(), Level.INFO, "LOG_MSG", isLoggingEnabled());
        }
    }
