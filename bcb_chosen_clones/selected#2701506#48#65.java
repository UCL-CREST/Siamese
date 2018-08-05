    public void recursiveDelete(File aDir) throws CErrorException {
        if (aDir == null || !aDir.exists() || !aDir.isDirectory()) {
            throw new RuntimeException("Implementation error! " + aDir + " invalid");
        }
        for (File vChild : aDir.listFiles()) {
            if (vChild.isFile()) {
                if (!vChild.delete()) {
                    throw new CErrorException(EErrorMessages.COULDNOTDELETE, vChild.getAbsolutePath());
                }
            } else if (vChild.isDirectory()) {
                recursiveDelete(vChild);
            }
        }
        logger.debug("Deleting " + aDir);
        if (!aDir.delete()) {
            throw new CErrorException(EErrorMessages.COULDNOTDELETE, aDir.getAbsolutePath());
        }
    }
