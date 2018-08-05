        public void actionPerformed(ActionEvent ev) {
            if (fileChooser == null) {
                fileChooser = new JFileChooser();
                ExtensionFileFilter fileFilter = new ExtensionFileFilter("Device profile (*.jar, *.zip)");
                fileFilter.addExtension("jar");
                fileFilter.addExtension("zip");
                fileChooser.setFileFilter(fileFilter);
            }
            if (fileChooser.showOpenDialog(SwingSelectDevicePanel.this) == JFileChooser.APPROVE_OPTION) {
                String manifestDeviceName = null;
                URL[] urls = new URL[1];
                ArrayList descriptorEntries = new ArrayList();
                JarFile jar = null;
                try {
                    jar = new JarFile(fileChooser.getSelectedFile());
                    Manifest manifest = jar.getManifest();
                    if (manifest != null) {
                        Attributes attrs = manifest.getMainAttributes();
                        manifestDeviceName = attrs.getValue("Device-Name");
                    }
                    for (Enumeration en = jar.entries(); en.hasMoreElements(); ) {
                        String entry = ((JarEntry) en.nextElement()).getName();
                        if ((entry.toLowerCase().endsWith(".xml") || entry.toLowerCase().endsWith("device.txt")) && !entry.toLowerCase().startsWith("meta-inf")) {
                            descriptorEntries.add(entry);
                        }
                    }
                    urls[0] = fileChooser.getSelectedFile().toURL();
                } catch (IOException e) {
                    Message.error("Error reading file: " + fileChooser.getSelectedFile().getName() + ", " + Message.getCauseMessage(e), e);
                    return;
                } finally {
                    if (jar != null) {
                        try {
                            jar.close();
                        } catch (IOException ignore) {
                        }
                    }
                }
                if (descriptorEntries.size() == 0) {
                    Message.error("Cannot find any device profile in file: " + fileChooser.getSelectedFile().getName());
                    return;
                }
                if (descriptorEntries.size() > 1) {
                    manifestDeviceName = null;
                }
                ClassLoader classLoader = Common.createExtensionsClassLoader(urls);
                HashMap devices = new HashMap();
                for (Iterator it = descriptorEntries.iterator(); it.hasNext(); ) {
                    String entryName = (String) it.next();
                    try {
                        devices.put(entryName, DeviceImpl.create(emulatorContext, classLoader, entryName, J2SEDevice.class));
                    } catch (IOException e) {
                        Message.error("Error parsing device profile, " + Message.getCauseMessage(e), e);
                        return;
                    }
                }
                for (Enumeration en = lsDevicesModel.elements(); en.hasMoreElements(); ) {
                    DeviceEntry entry = (DeviceEntry) en.nextElement();
                    if (devices.containsKey(entry.getDescriptorLocation())) {
                        devices.remove(entry.getDescriptorLocation());
                    }
                }
                if (devices.size() == 0) {
                    Message.info("Device profile already added");
                    return;
                }
                try {
                    File deviceFile = new File(Config.getConfigPath(), fileChooser.getSelectedFile().getName());
                    if (deviceFile.exists()) {
                        deviceFile = File.createTempFile("device", ".jar", Config.getConfigPath());
                    }
                    IOUtils.copyFile(fileChooser.getSelectedFile(), deviceFile);
                    DeviceEntry entry = null;
                    for (Iterator it = devices.keySet().iterator(); it.hasNext(); ) {
                        String descriptorLocation = (String) it.next();
                        Device device = (Device) devices.get(descriptorLocation);
                        if (manifestDeviceName != null) {
                            entry = new DeviceEntry(manifestDeviceName, deviceFile.getName(), descriptorLocation, false);
                        } else {
                            entry = new DeviceEntry(device.getName(), deviceFile.getName(), descriptorLocation, false);
                        }
                        lsDevicesModel.addElement(entry);
                        Config.addDeviceEntry(entry);
                    }
                    lsDevices.setSelectedValue(entry, true);
                } catch (IOException e) {
                    Message.error("Error adding device profile, " + Message.getCauseMessage(e), e);
                    return;
                }
            }
        }
