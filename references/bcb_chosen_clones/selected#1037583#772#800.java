        private File createWorkingCopy(File _originalRawDataFile) {
            File fOriginalRawDataFile = _originalRawDataFile;
            File fWorkingCopy;
            System.gc();
            try {
                fWorkingCopy = File.createTempFile("MZmine", null);
                FileChannel sourceChannel = new FileInputStream(fOriginalRawDataFile).getChannel();
                FileChannel destinationChannel = new FileOutputStream(fWorkingCopy).getChannel();
                long sourceChannelPos = 0;
                long sourceChannelSize = sourceChannel.size();
                long maxReadSize = 5 * 1024 * 1024;
                long targetChannelPos = 0;
                while (sourceChannelPos < sourceChannelSize) {
                    long transferAmount = maxReadSize;
                    if (transferAmount > (sourceChannelSize - sourceChannelPos)) {
                        transferAmount = sourceChannelSize - sourceChannelPos;
                    }
                    sourceChannel.transferTo(sourceChannelPos, transferAmount, destinationChannel);
                    sourceChannelPos += transferAmount;
                }
                sourceChannel.close();
                destinationChannel.close();
            } catch (Exception ekse) {
                Logger.put("NODE WORKER THREAD: ERROR - Failed to create working copy!");
                Logger.put(ekse.toString());
                return null;
            }
            return fWorkingCopy;
        }
