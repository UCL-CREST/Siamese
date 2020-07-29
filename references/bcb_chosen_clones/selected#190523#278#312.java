    public static void downloadJars(IProject project, String repositoryUrl, String jarDirectory, String[] jars) {
        try {
            File tmpFile = null;
            for (String jar : jars) {
                try {
                    tmpFile = File.createTempFile("tmpPlugin_", ".zip");
                    URL url = new URL(repositoryUrl + jarDirectory + jar);
                    String destFilename = new File(url.getFile()).getName();
                    File destFile = new File(project.getLocation().append("lib").append(jarDirectory).toFile(), destFilename);
                    InputStream inputStream = null;
                    FileOutputStream outputStream = null;
                    try {
                        URLConnection urlConnection = url.openConnection();
                        inputStream = urlConnection.getInputStream();
                        outputStream = new FileOutputStream(tmpFile);
                        IOUtils.copy(inputStream, outputStream);
                    } finally {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    }
                    FileUtils.copyFile(tmpFile, destFile);
                } finally {
                    if (tmpFile != null) {
                        tmpFile.delete();
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
