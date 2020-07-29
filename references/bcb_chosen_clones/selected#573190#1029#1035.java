    private static void exportConfigResource(ClassLoader classLoader, String resourceName, String targetFileName) throws Exception {
        InputStream is = classLoader.getResourceAsStream(resourceName);
        FileOutputStream fos = new FileOutputStream(targetFileName, false);
        IOUtils.copy(is, fos);
        fos.close();
        is.close();
    }
