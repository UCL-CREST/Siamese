    public static void copy(File from, File to) {
        boolean result;
        if (from.isDirectory()) {
            File[] subFiles = from.listFiles();
            for (int i = 0; i < subFiles.length; i++) {
                File newDir = new File(to, subFiles[i].getName());
                result = false;
                if (subFiles[i].isDirectory()) {
                    if (newDir.exists()) result = true; else result = newDir.mkdirs();
                } else if (subFiles[i].isFile()) {
                    try {
                        result = newDir.createNewFile();
                    } catch (IOException e) {
                        log.error("unable to create new file: " + newDir, e);
                        result = false;
                    }
                }
                if (result) copy(subFiles[i], newDir);
            }
        } else if (from.isFile()) {
            FileInputStream in = null;
            FileOutputStream out = null;
            try {
                in = new FileInputStream(from);
                out = new FileOutputStream(to);
                int fileLength = (int) from.length();
                char charBuff[] = new char[fileLength];
                int len;
                int oneChar;
                while ((oneChar = in.read()) != -1) {
                    out.write(oneChar);
                }
            } catch (FileNotFoundException e) {
                log.error("File not found!", e);
            } catch (IOException e) {
                log.error("Unable to read from file!", e);
            } finally {
                try {
                    if (in != null) in.close();
                    if (out != null) out.close();
                } catch (IOException e1) {
                    log.error("Error closing file reader/writer", e1);
                }
            }
        }
    }
