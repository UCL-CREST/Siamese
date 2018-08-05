    private boolean writeResource(PluginProxy eclipseInstallPlugin, ResourceProxy translation, LocaleProxy locale) throws Exception {
        String translationResourceName = determineTranslatedResourceName(translation, locale);
        String pluginNameInDirFormat = eclipseInstallPlugin.getName().replace(Messages.getString("Characters_period"), File.separator);
        if (translation.getRelativePath().contains(pluginNameInDirFormat)) {
            return writeResourceToBundleClasspath(translation, locale);
        } else if (translationResourceName.contains(File.separator)) {
            String resourcePath = translationResourceName.substring(0, translationResourceName.lastIndexOf(File.separatorChar));
            File resourcePathDirectory = new File(directory.getPath() + File.separatorChar + resourcePath);
            resourcePathDirectory.mkdirs();
        }
        File fragmentResource = new File(directory.getPath() + File.separatorChar + translationResourceName);
        File translatedResource = new File(translation.getFileResource().getAbsolutePath());
        FileChannel inputChannel = new FileInputStream(translatedResource).getChannel();
        FileChannel outputChannel = new FileOutputStream(fragmentResource).getChannel();
        inputChannel.transferTo(0, inputChannel.size(), outputChannel);
        inputChannel.close();
        outputChannel.close();
        return true;
    }
