    private void convertClasses(File source, File destination) throws PostProcessingException, CodeCheckException, IOException {
        Stack sourceStack = new Stack();
        Stack destinationStack = new Stack();
        sourceStack.push(source);
        destinationStack.push(destination);
        while (!sourceStack.isEmpty()) {
            source = (File) sourceStack.pop();
            destination = (File) destinationStack.pop();
            if (!destination.exists()) destination.mkdirs();
            File[] files = source.listFiles();
            for (int i = 0; i < files.length; i++) {
                File current = (File) files[i];
                if (current.isDirectory()) {
                    sourceStack.push(current);
                    destinationStack.push(new File(destination, current.getName()));
                } else if (current.getName().endsWith(".class")) {
                    ClassWriter writer = new ClassWriter();
                    InputStream is = new BufferedInputStream(new FileInputStream(current));
                    writer.readClass(is);
                    is.close();
                    if ((getStatusFlags(writer.getClassName(writer.getCurrentClassIndex())) & PP_PROCESSED) != 0) {
                        ClassWriter[] auxWriter = new ClassWriter[1];
                        transformClass(writer, auxWriter);
                        File output = new File(destination, current.getName());
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(output));
                        writer.writeClass(os);
                        os.close();
                        if (auxWriter[0] != null) {
                            String className = auxWriter[0].getClassName(auxWriter[0].getCurrentClassIndex());
                            className = className.substring(className.lastIndexOf('.') + 1, className.length());
                            output = new File(destination, className + ".class");
                            os = new BufferedOutputStream(new FileOutputStream(output));
                            auxWriter[0].writeClass(os);
                            os.close();
                        }
                    }
                }
            }
        }
    }
