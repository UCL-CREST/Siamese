    protected String saveJar2TmpFile(String jarUrl, boolean reportError) {
        InputStream is = null;
        try {
            URL url = new URL(jad.getJarURL());
            URLConnection conn = url.openConnection();
            if (url.getUserInfo() != null) {
                String userInfo = new String(Base64Coder.encode(url.getUserInfo().getBytes("UTF-8")));
                conn.setRequestProperty("Authorization", "Basic " + userInfo);
            }
            is = conn.getInputStream();
            File tmpDir = null;
            String systemTmpDir = MIDletSystemProperties.getSystemProperty("java.io.tmpdir");
            if (systemTmpDir != null) {
                tmpDir = new File(systemTmpDir, "microemulator-apps");
                if ((!tmpDir.exists()) && (!tmpDir.mkdirs())) {
                    tmpDir = null;
                }
            }
            File tmp = File.createTempFile("me2-app-", ".jar", tmpDir);
            tmp.deleteOnExit();
            IOUtils.copyToFile(is, tmp);
            return IOUtils.getCanonicalFileClassLoaderURL(tmp);
        } catch (IOException e) {
            if (reportError) {
                Message.error("Unable to open jar " + jarUrl, e);
            }
            return null;
        } finally {
            IOUtils.closeQuietly(is);
        }
    }
