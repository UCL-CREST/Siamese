    public static InputStream executeCommandAndGetInputStream(String command) {
        Process p = null;
        try {
            p = new ProcessBuilder(command).start();
        } catch (Exception ex) {
            try {
                p = Runtime.getRuntime().exec(command);
            } catch (Exception ex2) {
            }
        }
        if (p == null) return null;
        try {
            InputStream in = p.getInputStream();
            return in;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
