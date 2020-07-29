    public static boolean copyDirectoryCover(String srcDirName, String descDirName, boolean coverlay) {
        File srcDir = new File(srcDirName);
        if (!srcDir.exists()) {
            System.out.println("复制目录失败，源目录" + srcDirName + "不存在!");
            return false;
        } else if (!srcDir.isDirectory()) {
            System.out.println("复制目录失败，" + srcDirName + "不是一个目录!");
            return false;
        }
        if (!descDirName.endsWith(File.separator)) {
            descDirName = descDirName + File.separator;
        }
        File descDir = new File(descDirName);
        if (descDir.exists()) {
            if (coverlay) {
                System.out.println("目标目录已存在，准备删除!");
                if (!FileOperateUtils.delFile(descDirName)) {
                    System.out.println("删除目录" + descDirName + "失败!");
                    return false;
                }
            } else {
                System.out.println("目标目录复制失败，目标目录" + descDirName + "已存在!");
                return false;
            }
        } else {
            System.out.println("目标目录不存在，准备创建!");
            if (!descDir.mkdirs()) {
                System.out.println("创建目标目录失败!");
                return false;
            }
        }
        boolean flag = true;
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = FileOperateUtils.copyFile(files[i].getAbsolutePath(), descDirName + files[i].getName());
                if (!flag) {
                    break;
                }
            }
            if (files[i].isDirectory()) {
                flag = FileOperateUtils.copyDirectory(files[i].getAbsolutePath(), descDirName + files[i].getName());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            System.out.println("复制目录" + srcDirName + "到" + descDirName + "失败!");
            return false;
        }
        System.out.println("复制目录" + srcDirName + "到" + descDirName + "成功!");
        return true;
    }
