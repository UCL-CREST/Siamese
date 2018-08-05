    public static boolean copyFileCover(String srcFileName, String descFileName, boolean coverlay) {
        File srcFile = new File(srcFileName);
        if (!srcFile.exists()) {
            System.out.println("复制文件失败，源文件" + srcFileName + "不存在!");
            return false;
        } else if (!srcFile.isFile()) {
            System.out.println("复制文件失败，" + srcFileName + "不是一个文件!");
            return false;
        }
        File descFile = new File(descFileName);
        if (descFile.exists()) {
            if (coverlay) {
                System.out.println("目标文件已存在，准备删除!");
                if (!FileOperateUtils.delFile(descFileName)) {
                    System.out.println("删除目标文件" + descFileName + "失败!");
                    return false;
                }
            } else {
                System.out.println("复制文件失败，目标文件" + descFileName + "已存在!");
                return false;
            }
        } else {
            if (!descFile.getParentFile().exists()) {
                System.out.println("目标文件所在的目录不存在，创建目录!");
                if (!descFile.getParentFile().mkdirs()) {
                    System.out.println("创建目标文件所在的目录失败!");
                    return false;
                }
            }
        }
        int readByte = 0;
        InputStream ins = null;
        OutputStream outs = null;
        try {
            ins = new FileInputStream(srcFile);
            outs = new FileOutputStream(descFile);
            byte[] buf = new byte[1024];
            while ((readByte = ins.read(buf)) != -1) {
                outs.write(buf, 0, readByte);
            }
            System.out.println("复制单个文件" + srcFileName + "到" + descFileName + "成功!");
            return true;
        } catch (Exception e) {
            System.out.println("复制文件失败：" + e.getMessage());
            return false;
        } finally {
            if (outs != null) {
                try {
                    outs.close();
                } catch (IOException oute) {
                    oute.printStackTrace();
                }
            }
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException ine) {
                    ine.printStackTrace();
                }
            }
        }
    }
