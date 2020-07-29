    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Git git = Git.getCurrent(req.getSession());
        GitComponentReader gitReader = git.getComponentReader("warpinjector");
        String id = req.getParameter("id");
        GitElement element = gitReader.getElement(id);
        String path = (String) element.getAttribute("targetdir");
        File folder = new File(path);
        PrintWriter out = helper.getPrintWriter(resp);
        MessageBundle messageBundle = new MessageBundle("net.sf.warpcore.cms/servlets/InjectorServletMessages");
        Locale locale = req.getLocale();
        helper.header(out, messageBundle, locale);
        if (git.getUser() == null) {
            helper.notLoggedIn(out, messageBundle, locale);
        } else {
            try {
                MultiPartRequest request = new MultiPartRequest(req);
                FileInfo info = request.getFileInfo("userfile");
                File file = info.getFile();
                out.println("tempfile found: " + file.getPath() + "<br>");
                String fileName = info.getFileName();
                File target = new File(folder, fileName);
                out.println("copying tempfile to: " + target.getPath() + "<br>");
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream fos = new FileOutputStream(target);
                byte buf[] = new byte[1024];
                int n;
                while ((n = fis.read(buf)) > 0) fos.write(buf, 0, n);
                fis.close();
                fos.close();
                out.println("copy successful - deleting old tempfile<br>");
                out.println("deletion result. " + file.delete() + "<p>");
                out.println(messageBundle.getMessage("Done. The file {0} has been uploaded", new String[] { "'" + fileName + "'" }, locale));
                out.println("<p><a href=\"" + req.getRequestURI() + "?id=" + req.getParameter("id") + "\">" + messageBundle.getMessage("Click here to import another file.", locale) + "</a>");
            } catch (Exception ex) {
                out.println(messageBundle.getMessage("An error occured: {0}", new String[] { ex.getMessage() }, locale));
            }
        }
        helper.footer(out);
    }
