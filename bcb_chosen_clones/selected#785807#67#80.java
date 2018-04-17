    public void copyDependancyFiles() {
        for (String[] depStrings : getDependancyFiles()) {
            String source = depStrings[0];
            String target = depStrings[1];
            try {
                File sourceFile = PluginManager.getFile(source);
                IOUtils.copyEverything(sourceFile, new File(WEB_ROOT + target));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
