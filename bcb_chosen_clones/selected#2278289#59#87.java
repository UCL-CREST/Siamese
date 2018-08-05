    private List<String> createProjectInfoFile() throws SocketException, IOException {
        FTPClient client = new FTPClient();
        Set<String> projects = new HashSet<String>();
        client.connect("ftp.drupal.org");
        System.out.println("Connected to ftp.drupal.org");
        System.out.println(client.getReplyString());
        boolean loggedIn = client.login("anonymous", "info@regilo.org");
        if (loggedIn) {
            FTPFile[] files = client.listFiles("pub/drupal/files/projects");
            for (FTPFile file : files) {
                String name = file.getName();
                Pattern p = Pattern.compile("([a-zAZ_]*)-(\\d.x)-(.*)");
                Matcher m = p.matcher(name);
                if (m.matches()) {
                    String projectName = m.group(1);
                    String version = m.group(2);
                    if (version.equals("6.x")) {
                        projects.add(projectName);
                    }
                }
            }
        }
        List<String> projectList = new ArrayList<String>();
        for (String project : projects) {
            projectList.add(project);
        }
        Collections.sort(projectList);
        return projectList;
    }
