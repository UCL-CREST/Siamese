    private void deleteProject(String uid, String home, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String project = request.getParameter("project");
        String line;
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        htmlHeader(out, "Project Status", "");
        try {
            synchronized (Class.forName("com.sun.gep.SunTCP")) {
                Vector list = new Vector();
                String directory = home;
                Runtime.getRuntime().exec("/usr/bin/rm -rf " + directory + project);
                FilePermission perm = new FilePermission(directory + SUNTCP_LIST, "read,write,execute");
                File listfile = new File(directory + SUNTCP_LIST);
                BufferedReader read = new BufferedReader(new FileReader(listfile));
                while ((line = read.readLine()) != null) {
                    if (!((new StringTokenizer(line, "\t")).nextToken().equals(project))) {
                        list.addElement(line);
                    }
                }
                read.close();
                if (list.size() > 0) {
                    PrintWriter write = new PrintWriter(new BufferedWriter(new FileWriter(listfile)));
                    for (int i = 0; i < list.size(); i++) {
                        write.println((String) list.get(i));
                    }
                    write.close();
                } else {
                    listfile.delete();
                }
                out.println("The project was successfully deleted.");
            }
        } catch (Exception e) {
            out.println("Error accessing this project.");
        }
        out.println("<center><form><input type=button value=Continue onClick=\"opener.location.reload(); window.close()\"></form></center>");
        htmlFooter(out);
    }
