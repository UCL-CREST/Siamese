    public void copyRes(String from, String to) {
        File dir = new File(from);
        File list[];
        list = dir.listFiles();
        for (int i = 0; i < list.length; i++) {
            if (!list[i].isDirectory()) {
                try {
                    copyFile(from + list[i].getName(), to + list[i].getName());
                } catch (Exception ex) {
                    System.out.println("Can't copy file: " + from + list[i].getName());
                    System.out.println(ex.getLocalizedMessage());
                }
            } else {
                new File(to + list[i].getName()).mkdirs();
                copyRes(from + list[i].getName() + "/", to + list[i].getName() + "/");
            }
        }
    }
