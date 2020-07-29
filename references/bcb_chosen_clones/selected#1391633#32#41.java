    private static void ensure(File pFile) throws IOException {
        if (!pFile.exists()) {
            FileOutputStream fos = new FileOutputStream(pFile);
            String resourceName = "/" + pFile.getName();
            InputStream is = BaseTest.class.getResourceAsStream(resourceName);
            Assert.assertNotNull(String.format("Could not find resource [%s].", resourceName), is);
            IOUtils.copy(is, fos);
            fos.close();
        }
    }
