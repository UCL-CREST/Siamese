    private static void copyFile(String src, String dst) throws InvocationTargetException {
        try {
            FileChannel srcChannel;
            srcChannel = new FileInputStream(src).getChannel();
            FileChannel dstChannel = new FileOutputStream(dst).getChannel();
            dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
            srcChannel.close();
            dstChannel.close();
        } catch (FileNotFoundException e) {
            throw new InvocationTargetException(e, Messages.ALFWizardCreationAction_errorSourceFilesNotFound);
        } catch (IOException e) {
            throw new InvocationTargetException(e, Messages.ALFWizardCreationAction_errorCopyingFiles);
        }
    }
