    private static void deleteTree(File path) {
        for (File file : path.listFiles()) {
            if (file.isDirectory() && (!(file.getName().equals("libs")))) deleteTree(file); else if (!(file.getName().equals("JavaGenerator.jar"))) file.delete();
        }
        path.delete();
    }
