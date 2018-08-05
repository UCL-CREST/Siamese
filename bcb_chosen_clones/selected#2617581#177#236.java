    private boolean performModuleInstallation(Model m) {
        String seldir = directoryHandler.getSelectedDirectory();
        if (seldir == null) {
            MessageBox box = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
            box.setText("Cannot install");
            box.setMessage("A target directory must be selected.");
            box.open();
            return false;
        }
        String sjar = pathText.getText();
        File fjar = new File(sjar);
        if (!fjar.exists()) {
            MessageBox box = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
            box.setText("Cannot install");
            box.setMessage("A non-existing jar file has been selected.");
            box.open();
            return false;
        }
        int count = 0;
        try {
            URLClassLoader loader = new URLClassLoader(new URL[] { fjar.toURI().toURL() });
            JarInputStream jis = new JarInputStream(new FileInputStream(fjar));
            JarEntry entry = jis.getNextJarEntry();
            while (entry != null) {
                String name = entry.getName();
                if (name.endsWith(".class")) {
                    name = name.substring(0, name.length() - 6);
                    name = name.replace('/', '.');
                    Class<?> cls = loader.loadClass(name);
                    if (IAlgorithm.class.isAssignableFrom(cls) && !cls.isInterface() && (cls.getModifiers() & Modifier.ABSTRACT) == 0) {
                        if (!testAlgorithm(cls, m)) return false;
                        count++;
                    }
                }
                entry = jis.getNextJarEntry();
            }
        } catch (Exception e1) {
            Application.logexcept("Could not load classes from jar file.", e1);
            return false;
        }
        if (count == 0) {
            MessageBox box = new MessageBox(shell, SWT.ICON_WARNING | SWT.OK);
            box.setText("Cannot install");
            box.setMessage("There don't seem to be any algorithms in the specified module.");
            box.open();
            return false;
        }
        try {
            FileChannel ic = new FileInputStream(sjar).getChannel();
            FileChannel oc = new FileOutputStream(seldir + File.separator + fjar.getName()).getChannel();
            ic.transferTo(0, ic.size(), oc);
            ic.close();
            oc.close();
        } catch (Exception e) {
            Application.logexcept("Could not install module", e);
            return false;
        }
        result = new Object();
        return true;
    }
