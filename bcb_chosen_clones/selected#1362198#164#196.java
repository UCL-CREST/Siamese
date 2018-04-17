    private void copyXsl(File aTargetLogDir) {
        Trace.println(Trace.LEVEL.UTIL, "copyXsl( " + aTargetLogDir.getName() + " )", true);
        if (myXslSourceDir == null) {
            return;
        }
        File[] files = myXslSourceDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File srcFile = files[i];
            if (!srcFile.isDirectory()) {
                File tgtFile = new File(aTargetLogDir + File.separator + srcFile.getName());
                FileChannel inChannel = null;
                FileChannel outChannel = null;
                try {
                    inChannel = new FileInputStream(srcFile).getChannel();
                    outChannel = new FileOutputStream(tgtFile).getChannel();
                    inChannel.transferTo(0, inChannel.size(), outChannel);
                } catch (IOException e) {
                    throw new IOError(e);
                } finally {
                    if (inChannel != null) try {
                        inChannel.close();
                    } catch (IOException exc) {
                        throw new IOError(exc);
                    }
                    if (outChannel != null) try {
                        outChannel.close();
                    } catch (IOException exc) {
                        throw new IOError(exc);
                    }
                }
            }
        }
    }
