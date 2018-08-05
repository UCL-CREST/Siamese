    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter outPage = response.getWriter();
        outPage.println("<html>");
        outPage.println("<head><title>User Registration</title></head>");
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        List items;
        Properties propTransporter = new Properties();
        Properties propVBE = new Properties();
        propTransporter.load(this.getClass().getClassLoader().getResourceAsStream("transporter.properties"));
        propVBE.load(new FileInputStream(propTransporter.getProperty("path") + "vbe.properties"));
        String gramFolder = propTransporter.getProperty("path") + propTransporter.getProperty("gramfolder");
        try {
            items = upload.parseRequest((HttpServletRequest) request);
        } catch (FileUploadException e) {
            outPage.println("<h2>Error parsing the uploaded file: " + e.getMessage() + "</h2>");
            outPage.println("</body></html>");
            return;
        }
        String classFileName = null;
        String inFileName = null;
        String propFileName = null;
        String outFileName = null;
        String arguments = null;
        String classpath = null;
        String maxCpuTime = null;
        String maxMem = null;
        boolean stdout = false;
        boolean stderr = false;
        boolean scwLog = false;
        String nombre = null;
        FileItem item = null;
        if (items.size() > 0) {
            for (int x = 0; x < items.size(); x++) {
                item = (FileItem) items.get(x);
                nombre = item.getFieldName();
                try {
                    if (item.isFormField()) {
                        String valor = item.getString("UTF-8");
                        if (valor != null && !valor.equals("")) {
                            if (nombre.equals("arguments")) arguments = new String(valor); else if (nombre.equals("classpath")) classpath = new String(valor); else if (nombre.equals("outFile")) outFileName = new String(valor); else if (nombre.equals("maxCpuTime")) maxCpuTime = new String(valor); else if (nombre.equals("maxMem")) maxMem = new String(valor); else if (nombre.equals("stdout")) stdout = true; else if (nombre.equals("stderr")) stderr = true; else if (nombre.equals("scwLog")) scwLog = true;
                        }
                    } else {
                        if (nombre.equals("classFile")) {
                            classFileName = item.getName();
                            FileOutputStream out = new FileOutputStream(gramFolder + classFileName);
                            out.write(item.get());
                            out.close();
                        } else if (nombre.equals("inFile")) {
                            inFileName = item.getName();
                            FileOutputStream out = new FileOutputStream(gramFolder + inFileName);
                            out.write(item.get());
                            out.close();
                        } else if (nombre.equals("PropFile")) {
                            propFileName = item.getName();
                            FileOutputStream out = new FileOutputStream(gramFolder + propFileName);
                            out.write(item.get());
                            out.close();
                        }
                    }
                } catch (Exception e) {
                    outPage.println("<h2>Error getting the parameters or the file: " + e.getMessage() + "</h2>");
                    outPage.println("</body></html>");
                    return;
                }
            }
            GridTrustJobDescriptor descriptor = new GridTrustJobDescriptor("mng2.moviquity.com:2811", gramFolder, classFileName, arguments.split(","), classpath, inFileName, propFileName, outFileName, maxCpuTime, maxMem, stdout, stderr, scwLog);
            descriptor.writeToFile(gramFolder + "job.xml");
            String jobDescriptorPath = gramFolder + "job.xml";
            String voCertificatePath = "/var/tmp/pruebasgridtrust/asm35@moviquity.com_MovVO34.pem";
            String proxyCertificatePath = "/var/tmp/pruebasgridtrust/asm35@moviquity.com.pem";
            String command = "/usr/local/gridtrust/globus/bin/globusrun-ws -F http://146.48.96.75:4444/wsrf/services/ManagedJobFactoryService -submit -S -f " + jobDescriptorPath;
            String classPath = new BufferedReader(new FileReader("/tmp/classpath.txt")).readLine();
            String[] env = new String[2];
            env[0] = "GLOBUS_LOCATION=/usr/local/gridtrust/globus";
            env[1] = "LD_LIBRARY_PATH=/usr/local/gridtrust/globus/lib/";
            Runtime runtime = Runtime.getRuntime();
            Process job = runtime.exec(command, env);
            StreamReaderThread errorThread = new StreamReaderThread(job.getErrorStream());
            errorThread.start();
            StreamReaderThread outputThread = new StreamReaderThread(job.getInputStream());
            outputThread.start();
            try {
                job.waitFor();
            } catch (Exception e) {
                outPage.println("Error: " + e.getMessage());
            }
            try {
                errorThread.join();
                outputThread.join();
            } catch (Exception e) {
                outPage.println("Error: " + e.getMessage());
            }
            outPage.println(errorThread.getText());
            outPage.println(outputThread.getText());
            outPage.println("</body></html>");
        }
    }
