    public void execute() {
        try {
            if (methods == null) {
                return;
            }
            List<Resource> resources = new ArrayList<Resource>();
            for (Iterator<Resource> i = classFiles.iterator(); i.hasNext(); ) {
                final Resource classFile = i.next();
                if (!classFile.getName().equals(CALLSTACK_FILE)) {
                    try {
                        InputStream inputStream = classFile.getInputStream();
                        ClassReader reader = new ClassReader(inputStream);
                        reader.accept(new MethodAnalyzer(), 0);
                        resources.add(classFile);
                        oldSize += classFile.getSize();
                    } catch (IllegalStateException e) {
                    }
                }
            }
            for (Iterator<Resource> i = resources.iterator(); i.hasNext(); ) {
                final Resource classFile = i.next();
                try {
                    InputStream inputStream = classFile.getInputStream();
                    ClassReader reader = new ClassReader(inputStream);
                    ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
                    if (!classFile.getName().startsWith(CALLSTACK_FILE.substring(0, CALLSTACK_FILE.indexOf('.')))) {
                        reader.accept(new MethodInjector(writer), 0);
                        byte[] b = writer.toByteArray();
                        inputStream.close();
                        OutputStream outputStream = classFile.getOutputStream();
                        outputStream.write(b);
                        outputStream.close();
                        injectedClasses++;
                    }
                } catch (IllegalStateException e) {
                }
                newSize += classFile.getSize();
            }
            File classFile = new File(classFiles.getDir(), CALLSTACK_FILE);
            if (!classFile.exists()) {
                throw new BuildException("Missing net.yura.mobile.logging.CallStack class");
            }
            InputStream inputStream = new FileInputStream(classFile);
            ClassReader reader = new ClassReader(inputStream);
            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
            try {
                reader.accept(new PushMethodGenerator(writer), 0);
            } finally {
                inputStream.close();
            }
            byte[] b = writer.toByteArray();
            inputStream.close();
            OutputStream outputStream = new FileOutputStream(classFile);
            outputStream.write(b);
            outputStream.close();
            log(injectedClasses + " classes injected");
            log((injectedMethods * 100) / Math.max(1, totalMethods) + "% of methods injected");
            log((100 - ((oldSize * 100) / newSize)) + "% code size increase");
        } catch (IOException e) {
            log(e, 0);
        }
    }
