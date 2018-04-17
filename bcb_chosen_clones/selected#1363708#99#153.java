    private boolean addInstrumentationToArchive(CoberturaFile file, ZipInputStream archive, ZipOutputStream output) throws Throwable {
        boolean modified = false;
        ZipEntry entry;
        while ((entry = archive.getNextEntry()) != null) {
            try {
                String entryName = entry.getName();
                if (ArchiveUtil.isSignatureFile(entry.getName())) {
                    continue;
                }
                ZipEntry outputEntry = new ZipEntry(entry.getName());
                outputEntry.setComment(entry.getComment());
                outputEntry.setExtra(entry.getExtra());
                outputEntry.setTime(entry.getTime());
                output.putNextEntry(outputEntry);
                byte[] entryBytes = IOUtil.createByteArrayFromInputStream(archive);
                if ((classPattern.isSpecified()) && ArchiveUtil.isArchive(entryName)) {
                    Archive archiveObj = new Archive(file, entryBytes);
                    addInstrumentationToArchive(archiveObj);
                    if (archiveObj.isModified()) {
                        modified = true;
                        entryBytes = archiveObj.getBytes();
                        outputEntry.setTime(System.currentTimeMillis());
                    }
                } else if (isClass(entry) && classPattern.matches(entryName)) {
                    try {
                        ClassReader cr = new ClassReader(entryBytes);
                        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                        ClassInstrumenter cv = new ClassInstrumenter(projectData, cw, ignoreRegexes, ignoreBranchesRegexes, ignoreMethodAnnotations, ignoreTrivial);
                        cr.accept(cv, 0);
                        if (cv.isInstrumented()) {
                            logger.debug("Putting instrumented entry: " + entry.getName());
                            entryBytes = cw.toByteArray();
                            modified = true;
                            outputEntry.setTime(System.currentTimeMillis());
                        }
                    } catch (Throwable t) {
                        if (entry.getName().endsWith("_Stub.class")) {
                            logger.debug("Problems instrumenting archive entry: " + entry.getName(), t);
                        } else {
                            logger.warn("Problems instrumenting archive entry: " + entry.getName(), t);
                        }
                    }
                }
                output.write(entryBytes);
                output.closeEntry();
                archive.closeEntry();
            } catch (Exception e) {
                logger.warn("Problems with archive entry: " + entry.getName(), e);
            } catch (Throwable t) {
                logger.warn("Problems with archive entry: " + entry.getName(), t);
            }
            output.flush();
        }
        return modified;
    }
