    public static boolean unlockPackages(String target) {
        boolean success = false;
        try {
            Process prc;
            prc = Runtime.getRuntime().exec((SETACLEXE + "'-on HKLM\\" + (target == null ? "" : (target + "\\")) + "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Component Based Servicing\\Packages'-ot reg'-actn setowner'-ownr n:S-1-1-0;s:y'-rec yes").split("'"));
            int c;
            BufferedInputStream bis = new BufferedInputStream(prc.getInputStream());
            prc.getOutputStream().write('n');
            success = prc.waitFor() == 0;
            while ((c = bis.read()) != -1) {
                System.out.print((char) c);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(SetACL.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SetACL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }
