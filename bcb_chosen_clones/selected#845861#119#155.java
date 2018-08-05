    public void actionPerformed(ActionEvent e) {
        Date expireDate = null;
        File file = null;
        String enterpriseName = enterpriseNameTextField.getText();
        String expireTime = expireTimeTextField.getText();
        String filePath = filePathTextField.getText();
        long expireMills = 0L;
        if (!"-1".equals(expireTime)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                expireDate = sdf.parse(expireTime);
                expireMills = expireDate.getTime();
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        } else {
            expireMills = -1L;
        }
        if (expireMills != 0L && filePath != null && !filePath.trim().equals("") && enterpriseName != null && !enterpriseName.trim().equals("")) {
            EnterpriseImpl s = new EnterpriseImpl(expireMills, enterpriseName);
            file = new File(filePath);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                ZipOutputStream zos = new ZipOutputStream(fos);
                ZipEntry ze = new ZipEntry(expireMills + "");
                zos.putNextEntry(ze);
                ObjectOutputStream oos = new ObjectOutputStream(zos);
                oos.writeObject(s);
                oos.close();
                zos.close();
                fos.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        this.dispose();
    }
