    private Bundle getBundle() {
        Plugin plugin = pluginAccessor.getPlugin(TEMPLATES_SYMBOLIC_NAME);
        if (plugin == null) {
            plugin = pluginAccessor.getPlugin(TEMPLATES_PLUGIN_KEY);
        }
        if (plugin == null) {
            try {
                File tmpFile = File.createTempFile(TEMPLATES_SYMBOLIC_NAME, ".jar");
                ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(tmpFile));
                zout.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
                Manifest mf = new Manifest();
                mf.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1");
                mf.getMainAttributes().putValue(Constants.BUNDLE_SYMBOLICNAME, TEMPLATES_SYMBOLIC_NAME);
                mf.getMainAttributes().putValue(Constants.BUNDLE_VERSION, "1");
                mf.getMainAttributes().putValue(Constants.BUNDLE_DESCRIPTION, "Customized templates for the auto export plugin");
                mf.getMainAttributes().putValue(Constants.BUNDLE_NAME, "Auto export templates plugin");
                mf.getMainAttributes().putValue(Constants.BUNDLE_MANIFESTVERSION, "2");
                mf.write(zout);
                zout.close();
                pluginController.installPlugin(new JarPluginArtifact(tmpFile));
                tmpFile.delete();
            } catch (IOException e) {
                throw new RuntimeException("Unable to retrieve bundle", e);
            }
        }
        Bundle[] bundles = bundleContext.getBundles();
        for (Bundle bundle : bundles) {
            if (bundle.getSymbolicName().equals(TEMPLATES_SYMBOLIC_NAME)) {
                return bundle;
            }
        }
        throw new IllegalStateException("The templates bundle is not found");
    }
