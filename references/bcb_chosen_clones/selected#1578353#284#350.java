    public final String[] getPolicyName() throws FileNotFoundException {
        Class cerca = null;
        File f;
        if (this.memoryType_ == MemoryController.MemoryType.PAGED) {
            f = new File(this.replacementPolicyPath_);
        } else {
            f = new File(this.allocationPolicyPath_);
        }
        if (!f.exists()) {
            throw new FileNotFoundException("File .class " + "not found in " + f.getAbsolutePath());
        }
        FilenameFilter pathFilter = new Filter(extension_);
        String[] files = f.list(pathFilter);
        Vector<String> a = new Vector<String>();
        for (int i = 0; i < files.length; i++) {
            a.add(files[i]);
        }
        Collections.sort(a);
        files = new String[a.size()];
        a.toArray(files);
        Vector<String> tmpFiles = new Vector<String>();
        int k = 0;
        for (int i = 0; i < files.length; i++) {
            Constructor<Object> c;
            Object x;
            try {
                String tmp = files[i].substring(0, files[i].lastIndexOf("."));
                if (this.memoryType_ == MemoryController.MemoryType.PAGED) {
                    cerca = simpleClassLoader.findClass(tmp, this.memoryType_);
                    if (cerca.getConstructors().length != 0) {
                        c = (Constructor<Object>) cerca.getConstructors()[0];
                        if (c.getParameterTypes().length == 0) {
                            x = c.newInstance();
                        } else {
                            x = new Object();
                        }
                        if (x instanceof ReplacementPolicy) {
                            tmpFiles.add(tmp);
                            k++;
                        }
                    }
                } else {
                    cerca = simpleClassLoader.findClass(tmp, this.memoryType_);
                    if (cerca.getConstructors().length != 0) {
                        c = (Constructor<Object>) cerca.getConstructors()[0];
                        if (c.getParameterTypes().length != 0) {
                            x = c.newInstance(new MemoryModel(100));
                        } else {
                            x = new Object();
                        }
                        if (x instanceof AllocationPolicy) {
                            tmpFiles.add(tmp);
                            k++;
                        }
                    }
                }
            } catch (NoClassDefFoundError e) {
                System.out.println("QUA NEL METODO");
            } catch (Exception e) {
            }
        }
        if (tmpFiles.size() <= 0) {
            throw new FileNotFoundException("Nessuna politica trovata");
        }
        files = new String[tmpFiles.size()];
        return tmpFiles.toArray(files);
    }
