    private boolean writeResourceToBundleClasspath(ResourceProxy translation, LocaleProxy locale) throws Exception {
        if (bundleClasspathStream == null) {
            bundleClasspathStream = new JarOutputStream(new FileOutputStream(directory.getPath() + File.separator + BUNDLE_CLASSPATH));
        }
        byte[] buf = new byte[1024];
        FileInputStream in = new FileInputStream(new File(translation.getFileResource().getAbsolutePath()));
        String temp = determineTranslatedResourceName(translation, locale);
        bundleClasspathStream.putNextEntry(new ZipEntry(temp));
        int len;
        while ((len = in.read(buf)) > 0) {
            bundleClasspathStream.write(buf, 0, len);
        }
        bundleClasspathStream.closeEntry();
        in.close();
        return true;
    }
