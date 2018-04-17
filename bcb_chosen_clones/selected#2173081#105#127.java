        public void mouseClicked(MouseEvent e) {
            String userNameString;
            String passwordString;
            String md5String = "";
            MessageDigest md;
            userNameString = userNameJTextField.getText();
            passwordString = passwordJTextField.getText();
            try {
                md = MessageDigest.getInstance("MD5");
                md.update(passwordString.getBytes());
                md5String = new String(md.digest());
                md5String = Base64.encode(md5String.getBytes());
                System.out.println(md5String);
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
                System.out.println("ʧ��");
            }
            if (userNameString.equals("zouwulingde") && md5String.equals("aKdtP09kSTkWCD/cylk=")) {
                JOptionPane.showMessageDialog(null, "��ӭʹ��ѧ���ڹ���ϵͳ��");
            } else {
                JOptionPane.showMessageDialog(null, "�û�������벻��ȷ!!!!");
            }
        }
