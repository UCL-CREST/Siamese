    private void scrubOutputPath(String path) {
        File myPath = new File(path);
        File list[] = myPath.listFiles();
        if (list == null) return;
        for (int i = 0; i < list.length; i++) {
            File current = list[i];
            if (current.isDirectory()) {
                scrubOutputPath(current.getAbsolutePath());
                current.delete();
            } else if (current.isFile() && current.getName().endsWith(".java")) current.delete();
        }
    }
