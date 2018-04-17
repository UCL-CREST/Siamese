    private void playAudio(String mediaUrl) {
        try {
            URLConnection cn = new URL(mediaUrl).openConnection();
            InputStream is = cn.getInputStream();
            mediaFile = new File(this.getCacheDir(), "mediafile");
            FileOutputStream fos = new FileOutputStream(mediaFile);
            byte buf[] = new byte[16 * 1024];
            Log.i("FileOutputStream", "Download");
            do {
                int numread = is.read(buf);
                if (numread <= 0) break;
                fos.write(buf, 0, numread);
            } while (true);
            fos.flush();
            fos.close();
            Log.i("FileOutputStream", "Saved");
            MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener() {

                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    Log.i("MediaPlayer.OnCompletionListener", "MediaPlayer Released");
                }
            };
            mixPlayer.setOnCompletionListener(listener);
            FileInputStream fis = new FileInputStream(mediaFile);
            mixPlayer.setDataSource(fis.getFD());
            mixPlayer.prepare();
            Log.i("MediaPlayer", "Start Player");
            loading = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
