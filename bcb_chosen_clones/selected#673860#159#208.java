    @Override
    public void runTask(HashMap pjobParameters) throws Exception {
        if (hasRequiredResources(isSubTask())) {
            File lfileSource = new File(getSource());
            File lfileTarget = new File(getTarget());
            FileChannel lfisInput = null;
            FileChannel lfosOutput = null;
            try {
                int mbCount = 64;
                boolean lblnDone = false;
                while (!lblnDone) {
                    lfisInput = new FileInputStream(lfileSource).getChannel();
                    lfosOutput = new FileOutputStream(lfileTarget).getChannel();
                    try {
                        int maxCount = (mbCount * 1024 * 1024) - (32 * 1024);
                        long size = lfisInput.size();
                        long position = 0;
                        while (position < size) {
                            position += lfisInput.transferTo(position, maxCount, lfosOutput);
                        }
                        lblnDone = true;
                    } catch (IOException lioXcp) {
                        getLog().warn(lioXcp);
                        if (lioXcp.getMessage().contains("Insufficient system resources exist to complete the requested servic")) {
                            mbCount--;
                            getLog().debug("Dropped resource count down to [" + mbCount + "]");
                            if (mbCount == 0) {
                                lblnDone = true;
                            }
                            if (lfisInput != null) {
                                lfisInput.close();
                            }
                            if (lfosOutput != null) {
                                lfosOutput.close();
                            }
                        } else {
                            throw lioXcp;
                        }
                    }
                }
            } finally {
                if (lfisInput != null) {
                    lfisInput.close();
                }
                if (lfosOutput != null) {
                    lfosOutput.close();
                }
            }
        }
    }
