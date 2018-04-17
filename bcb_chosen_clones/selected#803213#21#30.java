    public File createReadmeFile(File dir, MavenProject mavenProject) throws IOException {
        InputStream is = getClass().getResourceAsStream("README.template");
        StringWriter sw = new StringWriter();
        IOUtils.copy(is, sw);
        String content = sw.getBuffer().toString();
        content = StringUtils.replace(content, "{project_name}", mavenProject.getArtifactId());
        File readme = new File(dir, "README.TXT");
        FileUtils.writeStringToFile(readme, content);
        return readme;
    }
