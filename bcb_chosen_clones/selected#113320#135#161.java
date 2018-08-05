    public static CodeBlock instantiateClass(ClassFile cf) throws InstantiationException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            cf.write(bos);
        } catch (IOException e) {
            LOGGING.log(Level.WARNING, "class file couldn't be written out", e);
        }
        byte[] classBytes = bos.toByteArray();
        Class codeBlockClass = currentClassLoader.createClass(cf.getClassName(), classBytes);
        if (saveClasses) {
            try {
                ZipEntry entry = new ZipEntry(cf.getClassName().replace('.', '/') + ".class");
                zip.putNextEntry(entry);
                zip.write(classBytes);
                zip.flush();
                zip.closeEntry();
            } catch (IOException ex) {
                Logger.getLogger(ClassFileBuilder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            return (CodeBlock) codeBlockClass.newInstance();
        } catch (IllegalAccessException e) {
            LOGGING.log(Level.WARNING, "insufficient access rights to instantiate class", e);
            throw (InstantiationException) new InstantiationException().initCause(e);
        }
    }
