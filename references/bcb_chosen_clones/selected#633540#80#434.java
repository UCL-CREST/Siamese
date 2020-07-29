    public void run() {
        StringBuffer xml;
        String tabName;
        Element guiElement;
        setBold(monitor.getReading());
        setBold(monitor.getReadingStatus());
        monitor.getReadingStatus().setText("      Working");
        HttpMethod method = null;
        xml = new StringBuffer();
        File tempfile = new File(url);
        if (tempfile.exists()) {
            try {
                InputStream in = new FileInputStream(tempfile);
                int temp;
                while ((temp = in.read()) != -1) {
                    xml.append((char) temp);
                }
                in.close();
            } catch (IOException e) {
                System.out.println("Loading Monitor Failed, error while reading XML file from local file");
                e.printStackTrace(System.err);
                return;
            }
        } else {
            try {
                HttpClient client = new HttpClient();
                method = new GetMethod(url);
                int response = client.executeMethod(method);
                if (response == 200) {
                    InputStream in = method.getResponseBodyAsStream();
                    int temp;
                    while ((temp = in.read()) != -1) {
                        xml.append((char) temp);
                    }
                    in.close();
                } else {
                    if (method != null) {
                        method.releaseConnection();
                    }
                    System.out.println("Loading Monitor Failed. Incorrect response from HTTP Server " + response);
                    return;
                }
            } catch (IOException e) {
                if (method != null) {
                    method.releaseConnection();
                }
                System.out.println("Loading Monitor Failed, error while reading XML file from HTTP Server");
                e.printStackTrace(System.err);
                return;
            }
        }
        setPlain(monitor.getReading());
        setPlain(monitor.getReadingStatus());
        monitor.getReadingStatus().setText("      Done");
        setBold(monitor.getValidating());
        setBold(monitor.getValidatingStatus());
        monitor.getValidatingStatus().setText("      Working");
        DocumentBuilderFactoryImpl factory = new DocumentBuilderFactoryImpl();
        try {
            DocumentBuilder parser = factory.newDocumentBuilder();
            Document document = parser.parse(new ByteArrayInputStream(xml.toString().getBytes()));
            if (method != null) {
                method.releaseConnection();
            }
            Element root = document.getDocumentElement();
            NodeList temp = root.getElementsByTagName("resource");
            for (int j = 0; j < temp.getLength(); j++) {
                Element resource = (Element) temp.item(j);
                resources.add(new URL(resource.getAttribute("url")));
            }
            NodeList connections = root.getElementsByTagName("jmxserver");
            for (int j = 0; j < connections.getLength(); j++) {
                Element connection = (Element) connections.item(j);
                String name = connection.getAttribute("name");
                String tempUrl = connection.getAttribute("url");
                String auth = connection.getAttribute("auth");
                if (tempUrl.indexOf("${host}") != -1) {
                    HostDialog dialog = new HostDialog(Config.getHosts());
                    String host = dialog.showDialog();
                    if (host == null) {
                        System.out.println("Host can not be null, unable to create panel.");
                        return;
                    }
                    tempUrl = tempUrl.replaceAll("\\$\\{host\\}", host);
                    Config.addHost(host);
                }
                JMXServiceURL jmxUrl = new JMXServiceURL(tempUrl);
                JmxServerGraph server = new JmxServerGraph(name, jmxUrl, new JmxWorker(false));
                if (auth != null && auth.equalsIgnoreCase("true")) {
                    LoginTrueService loginService = new LoginTrueService();
                    JXLoginPanel.Status status = JXLoginPanel.showLoginDialog(null, loginService);
                    if (status != JXLoginPanel.Status.SUCCEEDED) {
                        return;
                    }
                    server.setUsername(loginService.getName());
                    server.setPassword(loginService.getPassword());
                }
                servers.put(name, server);
                NodeList listeners = connection.getElementsByTagName("listener");
                for (int i = 0; i < listeners.getLength(); i++) {
                    Element attribute = (Element) listeners.item(i);
                    String taskname = attribute.getAttribute("taskname");
                    MBean mbean = new MBean(attribute.getAttribute("mbean"), null);
                    String filtertype = attribute.getAttribute("filterType");
                    TaskNotificationListener listener = new TaskNotificationListener();
                    NotificationFilterSupport filter = new NotificationFilterSupport();
                    if (filtertype == null || "".equals(filtertype)) {
                        filter = null;
                    } else {
                        filter.enableType(filtertype);
                    }
                    Task task = new Task(-1, Task.LISTEN, server);
                    task.setMbean(mbean);
                    task.setListener(listener);
                    task.setFilter(filter);
                    server.getWorker().addTask(task);
                    if (tasks.get(taskname) != null) {
                        System.out.println("Task " + taskname + " already exists.");
                        return;
                    }
                    List<Task> hashTempList = new ArrayList<Task>();
                    hashTempList.add(task);
                    tasks.put(taskname, hashTempList);
                }
                NodeList attributes = connection.getElementsByTagName("attribute");
                for (int i = 0; i < attributes.getLength(); i++) {
                    Element attribute = (Element) attributes.item(i);
                    String taskname = attribute.getAttribute("taskname");
                    MBean mbean = new MBean(attribute.getAttribute("mbean"), null);
                    String attributename = attribute.getAttribute("attributename");
                    String frequency = attribute.getAttribute("frequency");
                    String onEvent = attribute.getAttribute("onEvent");
                    if (frequency.equalsIgnoreCase("onchange")) {
                        TaskNotificationListener listener = new TaskNotificationListener();
                        AttributeChangeNotificationFilter filter = new AttributeChangeNotificationFilter();
                        filter.enableAttribute(attributename);
                        Task task = new Task(-1, Task.LISTEN, server);
                        MBeanAttribute att = new MBeanAttribute(mbean, attributename);
                        task.setAttribute(att);
                        task.setMbean(mbean);
                        task.setListener(listener);
                        task.setFilter(filter);
                        server.getWorker().addTask(task);
                        if (tasks.get(taskname) != null) {
                            System.out.println("Task " + taskname + " already exists.");
                            return;
                        }
                        Task task2 = new Task(-1, Task.GET_ATTRIBUTE, server);
                        task2.setAttribute(att);
                        task2.setMbean(mbean);
                        server.getWorker().addTask(task2);
                        List<Task> hashTempList = new ArrayList<Task>();
                        hashTempList.add(task);
                        hashTempList.add(task2);
                        tasks.put(taskname, hashTempList);
                    } else {
                        int frequency2 = Integer.parseInt(frequency);
                        Task task = new Task(frequency2, Task.GET_ATTRIBUTE, server);
                        MBeanAttribute att = new MBeanAttribute(mbean, attributename);
                        task.setAttribute(att);
                        task.setMbean(mbean);
                        if (tasks.get(taskname) != null) {
                            System.out.println("Task " + taskname + " already exists.");
                            return;
                        }
                        List<Task> hashTempList = new ArrayList<Task>();
                        hashTempList.add(task);
                        tasks.put(taskname, hashTempList);
                        TaskNotificationListener listener = null;
                        if (onEvent != null && !"".equals(onEvent)) {
                            Task tempTask = tasks.get(onEvent).get(0);
                            if (tempTask == null) {
                                System.out.println(onEvent + " was not found.");
                                return;
                            } else {
                                listener = (TaskNotificationListener) tempTask.getListener();
                            }
                        }
                        if (listener == null) {
                            server.getWorker().addTask(task);
                        } else {
                            listener.addTask(task);
                        }
                    }
                }
            }
            NodeList guiTemp = root.getElementsByTagName("gui");
            guiElement = (Element) guiTemp.item(0);
            tabName = guiElement.getAttribute("name");
            if (MonitorServer.contains(tabName)) {
                JOptionPane.showMessageDialog(null, "This panel is already open, stoping creating of panel.", "Panel already exists", JOptionPane.ERROR_MESSAGE);
                return;
            }
            for (int i = 0; i < monitor.getTab().getTabCount(); i++) {
                if (monitor.getTab().getComponent(i).equals(monitor)) {
                    monitor.getTab().setTitleAt(i, tabName);
                    break;
                }
            }
            NodeList tempBindings = root.getElementsByTagName("binding");
            for (int i = 0; i < tempBindings.getLength(); i++) {
                Element binding = (Element) tempBindings.item(i);
                String guiname = binding.getAttribute("guiname");
                String tmethod = binding.getAttribute("method");
                String taskname = binding.getAttribute("taskname");
                String formater = binding.getAttribute("formater");
                BindingContainer tempBinding;
                if (formater == null || (formater != null && formater.equals(""))) {
                    tempBinding = new BindingContainer(guiname, tmethod, taskname);
                } else {
                    tempBinding = new BindingContainer(guiname, tmethod, taskname, formater);
                }
                bindings.add(tempBinding);
            }
        } catch (Exception e) {
            System.err.println("Exception message: " + e.getMessage());
            System.out.println("Loading Monitor Failed, couldnt parse XML file.");
            e.printStackTrace(System.err);
            return;
        }
        setPlain(monitor.getValidating());
        setPlain(monitor.getValidatingStatus());
        monitor.getValidatingStatus().setText("      Done");
        setBold(monitor.getDownload());
        setBold(monitor.getDownloadStatus());
        monitor.getDownloadStatus().setText("      Working");
        List<File> jarFiles = new ArrayList<File>();
        File cacheDir = new File(Config.getCacheDir());
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        for (URL resUrl : resources) {
            try {
                HttpClient client = new HttpClient();
                HttpMethod methodRes = new GetMethod(resUrl.toString());
                int response = client.executeMethod(methodRes);
                if (response == 200) {
                    int index = resUrl.toString().lastIndexOf("/") + 1;
                    File file = new File(Config.getCacheDir() + resUrl.toString().substring(index));
                    FileOutputStream out = new FileOutputStream(file);
                    InputStream in = methodRes.getResponseBodyAsStream();
                    int readTemp = 0;
                    while ((readTemp = in.read()) != -1) {
                        out.write(readTemp);
                    }
                    System.out.println(file.getName() + " downloaded.");
                    methodRes.releaseConnection();
                    if (file.getName().endsWith(".jar")) {
                        jarFiles.add(file);
                    }
                } else {
                    methodRes.releaseConnection();
                    System.out.println("Loading Monitor Failed. Unable to get resource " + url);
                    return;
                }
            } catch (IOException e) {
                System.out.println("Loading Monitor Failed, error while reading resource file " + "from HTTP Server");
                e.printStackTrace(System.err);
                return;
            }
        }
        URL[] urls = new URL[jarFiles.size()];
        try {
            for (int i = 0; i < jarFiles.size(); i++) {
                File file = jarFiles.get(i);
                File newFile = new File(Config.getCacheDir() + "/" + System.currentTimeMillis() + file.getName());
                FileInputStream in = new FileInputStream(file);
                FileOutputStream out = new FileOutputStream(newFile);
                int n = 0;
                byte[] buf = new byte[1024];
                while ((n = in.read(buf, 0, 1024)) > -1) {
                    out.write(buf, 0, n);
                }
                out.close();
                out.close();
                in.close();
                urls[i] = new URL("file:" + newFile.getAbsolutePath());
            }
        } catch (Exception e1) {
            System.out.println("Unable to load jar files.");
            e1.printStackTrace();
        }
        URLClassLoader loader = new URLClassLoader(urls);
        engine.setClassLoader(loader);
        setPlain(monitor.getDownload());
        setPlain(monitor.getDownloadStatus());
        monitor.getDownloadStatus().setText("      Done");
        setBold(monitor.getGui());
        setBold(monitor.getGuiStatus());
        monitor.getGuiStatus().setText("      Working");
        Container container;
        try {
            String tempXml = xml.toString();
            int start = tempXml.indexOf("<gui");
            start = tempXml.indexOf('>', start) + 1;
            int end = tempXml.indexOf("</gui>");
            container = engine.render(new StringReader(tempXml.substring(start, end)));
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.err.println("Exception msg: " + e.getMessage());
            System.out.println("Loading Monitor Failed, error creating gui.");
            return;
        }
        for (BindingContainer bcon : bindings) {
            List<Task> temp = tasks.get(bcon.getTask());
            if (temp == null) {
                System.out.println("Task with name " + bcon.getTask() + " doesnt exist.");
            } else {
                for (Task task : temp) {
                    if (task != null) {
                        Object comp = engine.find(bcon.getComponent());
                        if (comp != null) {
                            if (task.getTaskType() == Task.LISTEN && task.getFilter() instanceof AttributeChangeNotificationFilter) {
                                TaskNotificationListener listener = (TaskNotificationListener) task.getListener();
                                if (bcon.getFormater() == null) {
                                    listener.addResultListener(new Binding(comp, bcon.getMethod()));
                                } else {
                                    listener.addResultListener(new Binding(comp, bcon.getMethod(), bcon.getFormater(), loader));
                                }
                            } else {
                                if (bcon.getFormater() == null) {
                                    task.addResultListener(new Binding(comp, bcon.getMethod()));
                                } else {
                                    task.addResultListener(new Binding(comp, bcon.getMethod(), bcon.getFormater(), loader));
                                }
                            }
                        } else {
                            System.out.println("Refering to gui name, " + bcon.getComponent() + ", that doesnt exist. Unable to create monitor.");
                            return;
                        }
                    } else {
                        System.out.println("Refering to task name, " + bcon.getTask() + ", that doesnt exist. Unable to create monitor.");
                        return;
                    }
                }
            }
        }
        for (int i = 0; i < monitor.getTab().getTabCount(); i++) {
            if (monitor.getTab().getComponent(i).equals(monitor)) {
                monitor.getTab().setComponentAt(i, new MonitorContainerPanel(container, this));
                break;
            }
        }
        System.out.println("Connecting to server(s).");
        Enumeration e = servers.keys();
        List<JmxWorker> list = new ArrayList<JmxWorker>();
        while (e.hasMoreElements()) {
            JmxWorker worker = servers.get(e.nextElement()).getWorker();
            worker.setRunning(true);
            worker.start();
            list.add(worker);
        }
        MonitorServer.add(tabName, list);
        Config.addUrl(url);
    }
