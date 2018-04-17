    public static void decompileClass(String className, ZipOutputStream destZip, String destDir, TabbedPrintWriter writer, ImportHandler imports) {
        try {
            ClassInfo clazz;
            try {
                clazz = ClassInfo.forName(className);
            } catch (IllegalArgumentException ex) {
                GlobalOptions.err.println("`" + className + "' is not a class name");
                return;
            }
            if (skipClass(clazz)) return;
            String filename = className.replace('.', File.separatorChar) + ".java";
            if (destZip != null) {
                writer.flush();
                destZip.putNextEntry(new ZipEntry(filename));
            } else if (destDir != null) {
                File file = new File(destDir, filename);
                File directory = new File(file.getParent());
                if (!directory.exists() && !directory.mkdirs()) {
                    GlobalOptions.err.println("Could not create directory " + directory.getPath() + ", check permissions.");
                }
                writer = new TabbedPrintWriter(new BufferedOutputStream(new FileOutputStream(file)), imports, false);
            }
            GlobalOptions.err.println(className);
            ClassAnalyzer clazzAna = new ClassAnalyzer(clazz, imports);
            clazzAna.dumpJavaFile(writer);
            if (destZip != null) {
                writer.flush();
                destZip.closeEntry();
            } else if (destDir != null) writer.close();
            System.gc();
            successCount++;
        } catch (IOException ex) {
            failedClasses.addElement(className);
            GlobalOptions.err.println("Can't write source of " + className + ".");
            GlobalOptions.err.println("Check the permissions.");
            ex.printStackTrace(GlobalOptions.err);
        } catch (Throwable t) {
            failedClasses.addElement(className);
            GlobalOptions.err.println("Failed to decompile " + className + ".");
            t.printStackTrace(GlobalOptions.err);
        }
    }
