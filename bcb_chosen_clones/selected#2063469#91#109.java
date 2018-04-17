    public void relativateFiles(String graphName, Node node, ZipOutputStream out) {
        if (node.getContent() instanceof ImageContent) {
            try {
                ImageContent content = (ImageContent) node.getContent();
                File file = content.getFile();
                String newFileName = graphName + ".files/" + file.getName();
                out.putNextEntry(new ZipEntry(newFileName));
                content.setFile(new File(newFileName));
                byte[] arr = new byte[(int) file.length()];
                (new FileInputStream(file)).read(arr, 0, (int) file.length());
                out.write(arr, 0, (int) file.length());
                out.closeEntry();
            } catch (IOException exc) {
            }
        }
        for (int i = 0; i < node.getChildren().size(); i++) {
            relativateFiles(graphName, (Node) node.getChildren().get(i), out);
        }
    }
