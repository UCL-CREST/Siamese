    public static boolean deltree(File dir) {
        final List<File> queue = new ArrayList<File>();
        queue.add(dir);
        boolean ok = true;
        int mark = -1;
        while (ok && queue.size() > 0) {
            File target = queue.remove(queue.size() - 1);
            if (target.isDirectory()) {
                if (mark == queue.size()) {
                    ok = target.delete();
                    mark = -1;
                } else {
                    mark = queue.size();
                    queue.add(target);
                    target.listFiles(new FileFilter() {

                        public boolean accept(File f) {
                            queue.add(f);
                            return false;
                        }
                    });
                }
            } else {
                ok = target.delete();
            }
        }
        return ok;
    }
