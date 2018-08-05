    public void retrieveLog() {
        setFilePath(txLocation.getText());
        String logPath = "";
        String envVarPath = "";
        try {
            java.util.Properties props = new java.util.Properties();
            java.net.URL url = new java.net.URL(reports.utility.NewGenLibDesktopRoot.getInstance().getURLRoot() + "/SystemFiles/Env_Var.txt");
            props.load(url.openStream());
            String jbossHomePath = props.getProperty("JBOSS_HOME");
            jbossHomePath = jbossHomePath.replaceAll("deploy", "log");
            logPath = jbossHomePath + "/server.log";
            FileInputStream fis = new FileInputStream(new File(jbossHomePath + "/server.log"));
            BufferedInputStream bis = new BufferedInputStream(fis, 2048);
            ZipEntry entry = new ZipEntry(jbossHomePath + "/server.log");
            FileOutputStream fos = new FileOutputStream(new File(getFilePath() + "/server.zip"));
            ZipOutputStream zout = new ZipOutputStream(new BufferedOutputStream(fos));
            zout.putNextEntry(entry);
            byte buffer[] = new byte[2048];
            int count;
            while ((count = bis.read(buffer, 0, 2048)) != -1) {
                zout.write(buffer, 0, count);
            }
            zout.close();
            fos.close();
            bis.close();
            fis.close();
            setStatus(false);
            callSuccess();
        } catch (java.io.FileNotFoundException fileExp) {
            javax.swing.JOptionPane.showMessageDialog(this, "System is unable to find the log file in the path\n   " + logPath + "\nPlease go to " + reports.utility.NewGenLibDesktopRoot.getRoot() + "/SystemFiles/Env_Var.txt," + "\nand check the path of the variable JBOSS_HOME. \nAlso, make sure this operation is being carried on the \nsystem where NewGenLib server is running.", "File not found", javax.swing.JOptionPane.ERROR_MESSAGE);
            setStatus(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
