    private byte[] getClassImplFromClassPath(String className) {
        try {
            String fileName = className.replace('.', IOTools.fs);
            Iterator it = classPathes.iterator();
            File f;
            while (it.hasNext()) {
                String classPath = (String) it.next();
                fileName = classPath + fileName + ".class";
                f = new File(fileName);
                if (f.exists()) {
                    if (debug) org.softnetwork.log.Log4jConnector.getConsole().debug("        >>>>>> Fetching the implementation of " + className);
                    return IOTools.getInstance().copyInputStream(new FileInputStream(f)).toByteArray();
                }
            }
            return null;
        } catch (Exception ex) {
            org.softnetwork.log.Log4jConnector.getConsole().error(ex.getMessage(), ex);
            return null;
        }
    }
