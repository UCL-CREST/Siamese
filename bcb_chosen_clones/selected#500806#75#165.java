    @Override
    public void execute(String[] args) throws Exception {
        Options cmdLineOptions = getCommandOptions();
        try {
            GnuParser parser = new GnuParser();
            CommandLine commandLine = parser.parse(cmdLineOptions, TolvenPlugin.getInitArgs());
            String srcRepositoryURLString = commandLine.getOptionValue(CMD_LINE_SRC_REPOSITORYURL_OPTION);
            Plugins libraryPlugins = RepositoryMetadata.getRepositoryPlugins(new URL(srcRepositoryURLString));
            String srcPluginId = commandLine.getOptionValue(CMD_LINE_SRC_PLUGIN_ID_OPTION);
            PluginDetail plugin = RepositoryMetadata.getPluginDetail(srcPluginId, libraryPlugins);
            if (plugin == null) {
                throw new RuntimeException("Could not locate plugin: " + srcPluginId + " in repository: " + srcRepositoryURLString);
            }
            String srcPluginVersionString = commandLine.getOptionValue(CMD_LINE_SRC_PLUGIN_VERSION_OPTION);
            PluginVersionDetail srcPluginVersion = null;
            if (srcPluginVersion == null) {
                srcPluginVersion = RepositoryMetadata.getLatestVersion(plugin);
            } else {
                srcPluginVersion = RepositoryMetadata.getPluginVersionDetail(srcPluginVersionString, plugin);
            }
            if (plugin == null) {
                throw new RuntimeException("Could not find a plugin version for: " + srcPluginId + " in repository: " + srcRepositoryURLString);
            }
            String destPluginId = commandLine.getOptionValue(CMD_LINE_DEST_PLUGIN_ID_OPTION);
            FileUtils.deleteDirectory(getPluginTmpDir());
            URL srcURL = new URL(srcPluginVersion.getUri());
            File newPluginDir = new File(getPluginTmpDir(), destPluginId);
            try {
                InputStream in = null;
                FileOutputStream out = null;
                File tmpZip = new File(getPluginTmpDir(), new File(srcURL.getFile()).getName());
                try {
                    in = srcURL.openStream();
                    out = new FileOutputStream(tmpZip);
                    IOUtils.copy(in, out);
                    TolvenZip.unzip(tmpZip, newPluginDir);
                } finally {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                    if (tmpZip != null) {
                        tmpZip.delete();
                    }
                }
                File pluginManifestFile = new File(newPluginDir, "tolven-plugin.xml");
                if (!pluginManifestFile.exists()) {
                    throw new RuntimeException(srcURL.toExternalForm() + "has no plugin manifest");
                }
                Plugin pluginManifest = RepositoryMetadata.getPlugin(pluginManifestFile.toURI().toURL());
                pluginManifest.setId(destPluginId);
                String destPluginVersion = commandLine.getOptionValue(CMD_LINE_DEST_PLUGIN_VERSION_OPTION);
                if (destPluginVersion == null) {
                    destPluginVersion = DEFAULT_DEST_VERSION;
                }
                pluginManifest.setVersion(destPluginVersion);
                String pluginManifestXML = RepositoryMetadata.getPluginManifest(pluginManifest);
                FileUtils.writeStringToFile(pluginManifestFile, pluginManifestXML);
                File pluginFragmentManifestFile = new File(newPluginDir, "tolven-plugin-fragment.xml");
                if (pluginFragmentManifestFile.exists()) {
                    PluginFragment pluginManifestFragment = RepositoryMetadata.getPluginFragment(pluginFragmentManifestFile.toURI().toURL());
                    Requires requires = pluginManifestFragment.getRequires();
                    if (requires == null) {
                        throw new RuntimeException("No <requires> detected for plugin fragment in: " + srcURL.toExternalForm());
                    }
                    if (requires.getImport().size() != 1) {
                        throw new RuntimeException("There should be only one import for plugin fragment in: " + srcURL.toExternalForm());
                    }
                    requires.getImport().get(0).setPluginId(destPluginId);
                    requires.getImport().get(0).setPluginVersion(destPluginVersion);
                    String pluginFragmentManifestXML = RepositoryMetadata.getPluginFragmentManifest(pluginManifestFragment);
                    FileUtils.writeStringToFile(pluginFragmentManifestFile, pluginFragmentManifestXML);
                }
                String destDirname = commandLine.getOptionValue(CMD_LINE_DEST_DIR_OPTION);
                File destDir = new File(destDirname);
                File destZip = new File(destDir, destPluginId + "-" + destPluginVersion + ".zip");
                destDir.mkdirs();
                TolvenZip.zip(newPluginDir, destZip);
            } finally {
                if (newPluginDir != null) {
                    FileUtils.deleteDirectory(newPluginDir);
                }
            }
        } catch (ParseException ex) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp(getClass().getName(), cmdLineOptions);
            throw new RuntimeException("Could not parse command line for: " + getClass().getName(), ex);
        }
    }
