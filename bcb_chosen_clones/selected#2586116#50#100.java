    public static synchronized String createWebStartFile(HttpServletRequest req, String SRBFilename, String workingDir, String contextPath) throws UnknownHostException {
        ServletRequest sreq = (ServletRequest) req;
        File file = new File(workingDir + File.separator + "web-start" + File.separator + "webstart_template.jnlp");
        File webStartDir = new File(workingDir + File.separator + "web-start" + File.separator + createDate());
        if (!webStartDir.exists()) webStartDir.mkdir();
        String filename = "" + Math.random() + ".jnlp";
        File webStartFile = new File(webStartDir, filename);
        BufferedReader in = null;
        BufferedWriter out = null;
        InetAddress host = InetAddress.getLocalHost();
        String hostURL = sreq.getScheme() + "://" + host.getHostAddress() + ":" + sreq.getServerPort() + contextPath + "/web-start/" + createDate();
        try {
            in = new BufferedReader(new FileReader(file));
            out = new BufferedWriter(new FileWriter(webStartFile));
            String str;
            while ((str = in.readLine()) != null) {
                if (str.trim().startsWith("codebase")) {
                    out.write("codebase=\"" + hostURL + "\"\n");
                } else if (str.trim().startsWith("href")) {
                    out.write("href=\"" + filename + "\">\n");
                } else if (str.trim().startsWith("<application-desc")) {
                    out.write(str);
                    out.write("\n<argument>" + hostURL + "/" + SRBFilename + "</argument>\n");
                    out.write("</application-desc>\n");
                    out.write("</jnlp>");
                    break;
                } else out.write(str + "\n");
            }
        } catch (IOException e) {
            System.out.println(e);
        } finally {
            try {
                in.close();
                out.close();
                File dir = new File(workingDir + File.separator + "web-start");
                File[] webStartDatefiles = dir.listFiles();
                for (File Sfile : webStartDatefiles) {
                    log.trace("File: " + Sfile.getAbsolutePath());
                    if (Sfile.isDirectory()) {
                        if (!Sfile.getName().equals(createDate())) {
                            log.trace("Deleting");
                            deleteDirectory(Sfile);
                        } else log.trace("Not deleting");
                    }
                }
            } catch (IOException ex) {
                log.warn("Error closing buffers", ex);
            }
        }
        return filename;
    }
