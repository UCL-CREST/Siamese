    public int checkVersion() {
        final int YEAR = 2012, MONTH = 1, DAY = 1;
        int versionCheck;
        try {
            StringBuffer os = new StringBuffer();
            os.append(System.getProperty("os.name").replace(' ', '_'));
            os.append("-");
            os.append(System.getProperty("os.arch").replace(' ', '_'));
            os.append("-");
            os.append(System.getProperty("os.version").replace(' ', '_'));
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("http://packjacket.sourceforge.net/vers.php?v=" + RunnerClass.VERSION + "&os=" + os.toString()).openStream()));
            int year = Integer.parseInt(in.readLine());
            int month = Integer.parseInt(in.readLine());
            int day = Integer.parseInt(in.readLine());
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = (Calendar) c1.clone();
            c2.setTime(c1.getTime());
            c1.set(YEAR, MONTH - 1, DAY);
            c2.set(year, month - 1, day);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMMM d, yyyy");
            RunnerClass.logger.info("This version was released on: " + sdf.format(c1.getTime()));
            RunnerClass.logger.info("The latest version was released on: " + sdf.format(c2.getTime()));
            if (c1.before(c2)) {
                RunnerClass.logger.info("You do not have the latest version.");
                versionCheck = OLD_VERSION;
            } else if (c1.after(c2)) {
                RunnerClass.logger.info("You have a version that is not publicly released yet.");
                versionCheck = NEW_VERSION;
            } else {
                RunnerClass.logger.info("You have the latest version.");
                versionCheck = SAME_VERSION;
            }
        } catch (Exception ex) {
            versionCheck = UNKNOWN_VERSION;
            RunnerClass.logger.info("PackJacket cannot determine the latest version because there is a most likely a problem " + "with your internet conection. The error is the following:");
            RunnerClass.logger.log(Level.SEVERE, null, ex);
        }
        if (versionCheck == OLD_VERSION) {
            int response = JOptionPane.showConfirmDialog(null, "You do not have the latest version.\nWould you like to download the new version?", "Version Check", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) if (Desktop.isDesktopSupported()) try {
                Desktop.getDesktop().browse(new URI("http://packjacket.sourceforge.net/download/"));
            } catch (Exception ex) {
                RunnerClass.logger.log(Level.SEVERE, null, ex);
            }
        }
        return versionCheck;
    }
