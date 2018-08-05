    private void render(String flame, String filename) {
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(Settings.flam3Folder + "flam3-render.exe");
            Map<String, String> env = pb.environment();
            env.put("out", filename);
            env.put("format", "jpg");
            Process p = pb.start();
            OutputStream os = p.getOutputStream();
            BufferedReader er = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            PrintWriter pw = new PrintWriter(os);
            pw.print(flame);
            pw.flush();
            pw.close();
            os.close();
            er.close();
            in.close();
            p.waitFor();
            p.destroy();
        } catch (InterruptedException ex) {
            Debugger.storeException(ex);
        } catch (IOException ex) {
            Debugger.storeException(ex);
        }
    }
