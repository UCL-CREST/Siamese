    protected void copyDependents() {
        for (File source : dependentFiles.keySet()) {
            try {
                if (!dependentFiles.get(source).exists()) {
                    if (dependentFiles.get(source).isDirectory()) dependentFiles.get(source).mkdirs(); else dependentFiles.get(source).getParentFile().mkdirs();
                }
                IOUtils.copyEverything(source, dependentFiles.get(source));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
