    public void copyAffix(MailAffix affix, long mailId1, long mailId2) throws Exception {
        File file = new File(this.getResDir(mailId1) + affix.getAttachAlias());
        if (file.exists()) {
            File file2 = new File(this.getResDir(mailId2) + affix.getAttachAlias());
            if (!file2.exists()) {
                file2.createNewFile();
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file2));
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                int read;
                while ((read = in.read()) != -1) {
                    out.write(read);
                }
                out.flush();
                in.close();
                out.close();
            }
        } else {
            log.debug(file.getAbsolutePath() + file.getName() + "�����ڣ������ļ�ʧ�ܣ���������");
        }
    }
