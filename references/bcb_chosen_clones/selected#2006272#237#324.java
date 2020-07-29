        public void actionPerformed(ActionEvent e) {
            if (saveForWebChooser == null) {
                ExtensionFileFilter fileFilter = new ExtensionFileFilter("HTML files");
                fileFilter.addExtension("html");
                saveForWebChooser = new JFileChooser();
                saveForWebChooser.setFileFilter(fileFilter);
                saveForWebChooser.setDialogTitle("Save for Web...");
                saveForWebChooser.setCurrentDirectory(new File(Config.getRecentDirectory("recentSaveForWebDirectory")));
            }
            if (saveForWebChooser.showSaveDialog(Main.this) == JFileChooser.APPROVE_OPTION) {
                Config.setRecentDirectory("recentSaveForWebDirectory", saveForWebChooser.getCurrentDirectory().getAbsolutePath());
                File pathFile = saveForWebChooser.getSelectedFile().getParentFile();
                String name = saveForWebChooser.getSelectedFile().getName();
                if (!name.toLowerCase().endsWith(".html") && name.indexOf('.') == -1) {
                    name = name + ".html";
                }
                String resource = MIDletClassLoader.getClassResourceName(this.getClass().getName());
                URL url = this.getClass().getClassLoader().getResource(resource);
                String path = url.getPath();
                int prefix = path.indexOf(':');
                String mainJarFileName = path.substring(prefix + 1, path.length() - resource.length());
                File appletJarDir = new File(new File(mainJarFileName).getParent(), "lib");
                File appletJarFile = new File(appletJarDir, "microemu-javase-applet.jar");
                if (!appletJarFile.exists()) {
                    appletJarFile = null;
                }
                if (appletJarFile == null) {
                }
                if (appletJarFile == null) {
                    ExtensionFileFilter fileFilter = new ExtensionFileFilter("JAR packages");
                    fileFilter.addExtension("jar");
                    JFileChooser appletChooser = new JFileChooser();
                    appletChooser.setFileFilter(fileFilter);
                    appletChooser.setDialogTitle("Select MicroEmulator applet jar package...");
                    appletChooser.setCurrentDirectory(new File(Config.getRecentDirectory("recentAppletJarDirectory")));
                    if (appletChooser.showOpenDialog(Main.this) == JFileChooser.APPROVE_OPTION) {
                        Config.setRecentDirectory("recentAppletJarDirectory", appletChooser.getCurrentDirectory().getAbsolutePath());
                        appletJarFile = appletChooser.getSelectedFile();
                    } else {
                        return;
                    }
                }
                JadMidletEntry jadMidletEntry;
                Iterator it = common.jad.getMidletEntries().iterator();
                if (it.hasNext()) {
                    jadMidletEntry = (JadMidletEntry) it.next();
                } else {
                    Message.error("MIDlet Suite has no entries");
                    return;
                }
                String midletInput = common.jad.getJarURL();
                DeviceEntry deviceInput = selectDevicePanel.getSelectedDeviceEntry();
                if (deviceInput != null && deviceInput.getDescriptorLocation().equals(DeviceImpl.DEFAULT_LOCATION)) {
                    deviceInput = null;
                }
                File htmlOutputFile = new File(pathFile, name);
                if (!allowOverride(htmlOutputFile)) {
                    return;
                }
                File appletPackageOutputFile = new File(pathFile, "microemu-javase-applet.jar");
                if (!allowOverride(appletPackageOutputFile)) {
                    return;
                }
                File midletOutputFile = new File(pathFile, midletInput.substring(midletInput.lastIndexOf("/") + 1));
                if (!allowOverride(midletOutputFile)) {
                    return;
                }
                File deviceOutputFile = null;
                String deviceDescriptorLocation = null;
                if (deviceInput != null) {
                    deviceOutputFile = new File(pathFile, deviceInput.getFileName());
                    if (!allowOverride(deviceOutputFile)) {
                        return;
                    }
                    deviceDescriptorLocation = deviceInput.getDescriptorLocation();
                }
                try {
                    AppletProducer.createHtml(htmlOutputFile, (DeviceImpl) DeviceFactory.getDevice(), jadMidletEntry.getClassName(), midletOutputFile, appletPackageOutputFile, deviceOutputFile);
                    AppletProducer.createMidlet(new URL(midletInput), midletOutputFile);
                    IOUtils.copyFile(appletJarFile, appletPackageOutputFile);
                    if (deviceInput != null) {
                        IOUtils.copyFile(new File(Config.getConfigPath(), deviceInput.getFileName()), deviceOutputFile);
                    }
                } catch (IOException ex) {
                    Logger.error(ex);
                }
            }
        }
