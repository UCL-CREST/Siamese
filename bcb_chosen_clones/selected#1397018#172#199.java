        @Override
        public void actionPerformed(ActionEvent e) {
            if (copiedFiles_ != null) {
                File[] tmpFiles = new File[copiedFiles_.length];
                File tmpDir = new File(Settings.getPropertyString(ConstantKeys.project_dir), "tmp/");
                tmpDir.mkdirs();
                for (int i = copiedFiles_.length - 1; i >= 0; i--) {
                    Frame f = FrameManager.getInstance().getFrameAtIndex(i);
                    try {
                        File in = f.getFile();
                        File out = new File(tmpDir, f.getFile().getName());
                        FileChannel inChannel = new FileInputStream(in).getChannel();
                        FileChannel outChannel = new FileOutputStream(out).getChannel();
                        inChannel.transferTo(0, inChannel.size(), outChannel);
                        if (inChannel != null) inChannel.close();
                        if (outChannel != null) outChannel.close();
                        tmpFiles[i] = out;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
                try {
                    FrameManager.getInstance().insertFrames(getTable().getSelectedRow(), FrameManager.INSERT_TYPE.MOVE, tmpFiles);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
