    public static boolean copyFileCover(String srcFileName, String descFileName, boolean coverlay) {
        File srcFile = new File(srcFileName);
        if (!srcFile.exists()) {
            System.out.println("�����ļ�ʧ�ܣ�Դ�ļ�" + srcFileName + "������!");
            return false;
        } else if (!srcFile.isFile()) {
            System.out.println("�����ļ�ʧ�ܣ�" + srcFileName + "����һ���ļ�!");
            return false;
        }
        File descFile = new File(descFileName);
        if (descFile.exists()) {
            if (coverlay) {
                System.out.println("Ŀ���ļ��Ѵ��ڣ�׼��ɾ��!");
                if (!FileOperateUtils.delFile(descFileName)) {
                    System.out.println("ɾ��Ŀ���ļ�" + descFileName + "ʧ��!");
                    return false;
                }
            } else {
                System.out.println("�����ļ�ʧ�ܣ�Ŀ���ļ�" + descFileName + "�Ѵ���!");
                return false;
            }
        } else {
            if (!descFile.getParentFile().exists()) {
                System.out.println("Ŀ���ļ����ڵ�Ŀ¼�����ڣ�����Ŀ¼!");
                if (!descFile.getParentFile().mkdirs()) {
                    System.out.println("����Ŀ���ļ����ڵ�Ŀ¼ʧ��!");
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
            System.out.println("���Ƶ����ļ�" + srcFileName + "��" + descFileName + "�ɹ�!");
            return true;
        } catch (Exception e) {
            System.out.println("�����ļ�ʧ�ܣ�" + e.getMessage());
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
