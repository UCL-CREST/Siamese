    private void initUserExtensions(SeleniumConfiguration seleniumConfiguration) throws IOException {
        StringBuilder contents = new StringBuilder();
        StringOutputStream s = new StringOutputStream();
        IOUtils.copy(SeleniumConfiguration.class.getResourceAsStream("default-user-extensions.js"), s);
        contents.append(s.toString());
        File providedUserExtensions = seleniumConfiguration.getFile(ConfigurationPropertyKeys.SELENIUM_USER_EXTENSIONS, seleniumConfiguration.getDirectoryConfiguration().getInput(), false);
        if (providedUserExtensions != null) {
            contents.append(FileUtils.readFileToString(providedUserExtensions, null));
        }
        seleniumUserExtensions = new File(seleniumConfiguration.getDirectoryConfiguration().getInput(), "user-extensions.js");
        FileUtils.forceMkdir(seleniumUserExtensions.getParentFile());
        FileUtils.writeStringToFile(seleniumUserExtensions, contents.toString(), null);
    }
