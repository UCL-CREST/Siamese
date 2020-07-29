    private String convertVideoToFlash(File imageFile) {
        String command = "";
        Process proc = null;
        Runtime rt = Runtime.getRuntime();
        String newFilename = "";
        String fileToConvert = imageFile.getAbsolutePath();
        System.out.println("Convert file: " + fileToConvert);
        newFilename = fileToConvert.substring(0, (fileToConvert.lastIndexOf("."))) + ".flv";
        command = "ffmpeg -i \"" + fileToConvert + "\" -s 320x240 -b 1000k -maxrate 4000k -bufsize 1024k \"" + newFilename + "\"";
        System.out.println("Convert command is: " + command);
        try {
            proc = rt.exec(command);
            StreamGobbler errorGobbler = new StreamGobbler(proc.getErrorStream(), "ffmpeg ERROR ");
            StreamGobbler outputGobbler = new StreamGobbler(proc.getInputStream(), "ffmpeg OUTPUT ");
            errorGobbler.start();
            outputGobbler.start();
            try {
                proc.waitFor();
            } catch (InterruptedException ex) {
                System.out.println("FFMpeg interrupted exeception: " + ex);
            }
        } catch (IOException ex1) {
            System.out.println("IOException converting video to flash: " + ex1);
        }
        return newFilename;
    }
