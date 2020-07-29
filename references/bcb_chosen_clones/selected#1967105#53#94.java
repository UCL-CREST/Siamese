    private void forBundle(BundleManipulator manip) {
        ByteArrayOutputStream bout = null;
        try {
            bout = new ByteArrayOutputStream();
            ZipOutputStream zout = new ZipOutputStream(bout);
            Bundle bundle = getBundle();
            Enumeration<URL> files = bundle.findEntries("/", "*.vm", false);
            if (files != null) {
                while (files.hasMoreElements()) {
                    URL url = files.nextElement();
                    String name = url.getFile();
                    if (name.startsWith("/")) {
                        name = name.substring(1);
                    }
                    if (manip.includeEntry(name)) {
                        zout.putNextEntry(new ZipEntry(name));
                        IOUtils.copy(url.openStream(), zout);
                    }
                }
            }
            manip.finish(bundle, zout);
            Manifest mf = new Manifest(bundle.getEntry("META-INF/MANIFEST.MF").openStream());
            zout.putNextEntry(new ZipEntry("META-INF/MANIFEST.MF"));
            mf.write(zout);
            zout.close();
            File tmpFile = File.createTempFile(TEMPLATES_SYMBOLIC_NAME, ".jar");
            FileUtils.writeByteArrayToFile(tmpFile, bout.toByteArray());
            if (pluginAccessor.getPlugin(TEMPLATES_SYMBOLIC_NAME) != null) {
                pluginController.uninstall(pluginAccessor.getPlugin(TEMPLATES_SYMBOLIC_NAME));
            } else if (pluginAccessor.getPlugin(TEMPLATES_PLUGIN_KEY) != null) {
                pluginController.uninstall(pluginAccessor.getPlugin(TEMPLATES_PLUGIN_KEY));
            }
            pluginController.installPlugin(new JarPluginArtifact(tmpFile));
            ServiceReference ref = bundleContext.getServiceReference(PackageAdmin.class.getName());
            ((PackageAdmin) bundleContext.getService(ref)).refreshPackages(null);
            tmpFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(bout);
        }
    }
