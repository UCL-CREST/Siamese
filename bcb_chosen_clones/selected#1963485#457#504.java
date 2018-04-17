    public void copyFile(String source, String destination, String description, boolean recursive) throws Exception {
        File sourceFile = new File(source);
        File destinationFile = new File(destination);
        if (!sourceFile.exists()) {
            throw new Exception("source file (" + source + ") does not exist!");
        }
        if (!sourceFile.isFile()) {
            throw new Exception("source file (" + source + ") is not a file!");
        }
        if (!sourceFile.canRead()) {
            throw new Exception("source file (" + source + ") is not readable!");
        }
        if (destinationFile.exists()) {
            m_out.print("  - " + destination + " exists, removing... ");
            if (destinationFile.delete()) {
                m_out.println("REMOVED");
            } else {
                m_out.println("FAILED");
                throw new Exception("unable to delete existing file: " + sourceFile);
            }
        }
        m_out.print("  - copying " + source + " to " + destination + "... ");
        if (!destinationFile.getParentFile().exists()) {
            if (!destinationFile.getParentFile().mkdirs()) {
                throw new Exception("unable to create directory: " + destinationFile.getParent());
            }
        }
        if (!destinationFile.createNewFile()) {
            throw new Exception("unable to create file: " + destinationFile);
        }
        FileChannel from = null;
        FileChannel to = null;
        try {
            from = new FileInputStream(sourceFile).getChannel();
            to = new FileOutputStream(destinationFile).getChannel();
            to.transferFrom(from, 0, from.size());
        } catch (FileNotFoundException e) {
            throw new Exception("unable to copy " + sourceFile + " to " + destinationFile, e);
        } finally {
            if (from != null) {
                from.close();
            }
            if (to != null) {
                to.close();
            }
        }
        m_out.println("DONE");
    }
