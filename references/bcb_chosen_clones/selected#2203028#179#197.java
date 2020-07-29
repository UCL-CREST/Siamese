    private void actionPlay() {
        DefaultMutableTreeNode selNd = getSelectedNode();
        if (selNd != null) {
            int[] selIdxs = getNodeIndexList(selNd);
            if (selIdxs.length == 2) {
                try {
                    Clip clip = AudioSystem.getClip();
                    clip.open(AudioVector.toAudioInputStream(audioVectors.get(selIdxs[0]).get(selIdxs[1]), audioSampleRate));
                    clip.start();
                    clip.drain();
                    clip.stop();
                    clip.flush();
                    clip.close();
                } catch (Exception exc) {
                    FastICAApp.exceptionDialog(exc);
                }
            }
        }
    }
