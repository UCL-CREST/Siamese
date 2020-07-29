    public RmRecResult rmRec(File x, RmRecResult result) {
        if (x.isDirectory()) {
            for (File f : x.listFiles()) {
                rmRec(f, result);
            }
        }
        if (!x.delete()) {
            result.failures.add(x);
        }
        return result;
    }
