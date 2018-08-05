    public static void savePresetPackage(PresetPackage pkg, File pkgFile, ProgressCallback prog) throws PackageGenerationException {
        try {
            if (!(pkg instanceof Impl_PresetPackage)) throw new PackageGenerationException("invalid preset package object");
            ZipOutputStream os = null;
            ObjectOutputStream oos = null;
            try {
                os = new ZipOutputStream(new FileOutputStream(pkgFile));
                os.setMethod(ZipOutputStream.DEFLATED);
                os.setLevel(Deflater.BEST_SPEED);
                os.putNextEntry(new ZipEntry(PresetPackage.PRESET_PKG_CONTENT_ENTRY));
                oos = new ObjectOutputStream(os);
                oos.writeObject(pkg.getHeader());
                try {
                    makeIsolatedPresetsSerializable((Impl_PresetPackage) pkg);
                } catch (PresetException e) {
                    throw new PackageGenerationException("General packaging error");
                }
                oos.writeObject(pkg);
                if (pkg.getSamplePackage() != null) {
                    File smplDir = ZUtilities.replaceExtension(pkgFile, SamplePackage.SAMPLE_DIR_EXT);
                    try {
                        os.putNextEntry(new ZipEntry(SamplePackage.SAMPLE_PKG_CONTENT_ENTRY));
                    } catch (IOException e) {
                        throw new PackageGenerationException(e.getMessage());
                    }
                    writeSamplePackage(pkg.getSamplePackage(), oos, smplDir, prog);
                }
                oos.close();
            } catch (IOException e) {
                pkgFile.delete();
                throw new PackageGenerationException(e.getMessage());
            }
        } finally {
            prog.updateProgress(1);
        }
    }
