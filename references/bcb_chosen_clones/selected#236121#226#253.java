    public boolean save(IFileHandle output) {
        if (output == null || output.canOpenOutput() == false) return false;
        try {
            logger.log(Level.FINE, "Saving ''{0}''", output.toString());
            OutputStream fos = output.openOutput();
            ZipOutputStream zos = new ZipOutputStream(fos);
            zos.putNextEntry(new ZipEntry("OK"));
            ObjectOutputStream out = new ObjectOutputStream(zos);
            SerialisedNode sDoc = new SerialisedNode();
            recursiveSerialise(head, sDoc);
            try {
                out.writeObject(sDoc);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Unable to write serialised node", ex);
                return false;
            }
            zos.closeEntry();
            out.close();
            zos.close();
            fos.close();
            needsSaving = false;
            logger.log(Level.FINE, "Saved ''{0}''", output.toString());
            return true;
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Problem writing to file", ex);
            return false;
        }
    }
