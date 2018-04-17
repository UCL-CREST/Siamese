    public static boolean copy(File source, File target, boolean owrite) {
        if (!source.exists()) {
            log.error("Invalid input to copy: source " + source + "doesn't exist");
            return false;
        } else if (!source.isFile()) {
            log.error("Invalid input to copy: source " + source + "isn't a file.");
            return false;
        } else if (target.exists() && !owrite) {
            log.error("Invalid input to copy: target " + target + " exists.");
            return false;
        }
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(source));
            BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(target));
            byte buffer[] = new byte[1024];
            int read = -1;
            while ((read = in.read(buffer, 0, 1024)) != -1) out.write(buffer, 0, read);
            out.flush();
            out.close();
            in.close();
            return true;
        } catch (IOException e) {
            log.error("Copy failed: ", e);
            return false;
        }
    }
