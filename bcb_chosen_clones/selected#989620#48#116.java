    public static Class evalToClass(Compilation comp, URL url) throws SyntaxException {
        ModuleExp mexp = comp.getModule();
        SourceMessages messages = comp.getMessages();
        try {
            comp.minfo.loadByStages(Compilation.COMPILED);
            if (messages.seenErrors()) return null;
            ArrayClassLoader loader = comp.loader;
            if (url == null) url = Path.currentPath().toURL();
            loader.setResourceContext(url);
            java.util.zip.ZipOutputStream zout = null;
            if (dumpZipPrefix != null) {
                StringBuffer zipname = new StringBuffer(dumpZipPrefix);
                lastZipCounter++;
                if (interactiveCounter > lastZipCounter) lastZipCounter = interactiveCounter;
                zipname.append(lastZipCounter);
                zipname.append(".zip");
                java.io.FileOutputStream zfout = new java.io.FileOutputStream(zipname.toString());
                zout = new java.util.zip.ZipOutputStream(zfout);
            }
            for (int iClass = 0; iClass < comp.numClasses; iClass++) {
                ClassType clas = comp.classes[iClass];
                String className = clas.getName();
                byte[] classBytes = clas.writeToArray();
                loader.addClass(className, classBytes);
                if (zout != null) {
                    String clname = className.replace('.', '/') + ".class";
                    java.util.zip.ZipEntry zent = new java.util.zip.ZipEntry(clname);
                    zent.setSize(classBytes.length);
                    java.util.zip.CRC32 crc = new java.util.zip.CRC32();
                    crc.update(classBytes);
                    zent.setCrc(crc.getValue());
                    zent.setMethod(java.util.zip.ZipEntry.STORED);
                    zout.putNextEntry(zent);
                    zout.write(classBytes);
                }
            }
            if (zout != null) {
                zout.close();
            }
            Class clas = null;
            ArrayClassLoader context = loader;
            while (context.getParent() instanceof ArrayClassLoader) context = (ArrayClassLoader) context.getParent();
            for (int iClass = 0; iClass < comp.numClasses; iClass++) {
                ClassType ctype = comp.classes[iClass];
                Class cclass = loader.loadClass(ctype.getName());
                ctype.setReflectClass(cclass);
                ctype.setExisting(true);
                if (iClass == 0) clas = cclass; else if (context != loader) context.addClass(cclass);
            }
            ModuleInfo minfo = comp.minfo;
            minfo.setModuleClass(clas);
            comp.cleanupAfterCompilation();
            int ndeps = minfo.numDependencies;
            for (int idep = 0; idep < ndeps; idep++) {
                ModuleInfo dep = minfo.dependencies[idep];
                Class dclass = dep.getModuleClassRaw();
                if (dclass == null) dclass = evalToClass(dep.comp, null);
                comp.loader.addClass(dclass);
            }
            return clas;
        } catch (java.io.IOException ex) {
            throw new WrappedException("I/O error in lambda eval", ex);
        } catch (ClassNotFoundException ex) {
            throw new WrappedException("class not found in lambda eval", ex);
        } catch (Throwable ex) {
            comp.getMessages().error('f', "internal compile error - caught " + ex, ex);
            throw new SyntaxException(messages);
        }
    }
