    public void testDigest() {
        try {
            String myinfo = "我的测试信息";
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            alga.update(myinfo.getBytes());
            byte[] digesta = alga.digest();
            System.out.println("本信息摘要是:" + byte2hex(digesta));
            MessageDigest algb = MessageDigest.getInstance("SHA-1");
            algb.update(myinfo.getBytes());
            if (MessageDigest.isEqual(digesta, algb.digest())) {
                System.out.println("信息检查正常");
            } else {
                System.out.println("摘要不相同");
            }
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("非法摘要算法");
        }
    }
