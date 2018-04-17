    public static void copyFile(File src, File dest, boolean notifyUserOnError) {
        if (src.exists()) {
            try {
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(dest));
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(src));
                byte[] read = new byte[128];
                int len = 128;
                while ((len = in.read(read)) > 0) out.write(read, 0, len);
                out.flush();
                out.close();
                in.close();
            } catch (IOException e) {
                String message = "Error while copying " + src.getAbsolutePath() + " to " + dest.getAbsolutePath() + " : " + e.getMessage();
                if (notifyUserOnError) {
                    Log.getInstance(SystemUtils.class).warnWithUserNotification(message);
                } else {
                    Log.getInstance(SystemUtils.class).warn(message);
                }
            }
        } else {
            String message = "Unable to copy file: source does not exists: " + src.getAbsolutePath();
            if (notifyUserOnError) {
                Log.getInstance(SystemUtils.class).warnWithUserNotification(message);
            } else {
                Log.getInstance(SystemUtils.class).warn(message);
            }
        }
    }
