    public void find(File src, Integer deep) throws IOException {
        if (src.isDirectory()) {
            File[] files = src.listFiles();
            for (File file : files) {
                if (deep == 1) {
                    subSrc = file.getAbsolutePath();
                }
                if (file.isDirectory()) {
                    find(file, ++deep);
                    if (file.delete()) {
                        System.out.println("刪除成功！");
                    }
                    deep--;
                } else {
                    if (deep == 2) {
                        file.renameTo(new File(file.getParent() + "\\" + generateName(++index)));
                    }
                    if (deep > 2) {
                        String dest = subSrc + "\\" + generateName(++index);
                        this.copy(file.getAbsolutePath(), dest);
                        if (file.delete()) {
                            System.out.println("刪除成功！");
                        }
                    }
                }
            }
        } else {
            System.err.println("根文件路徑有誤！");
        }
    }
