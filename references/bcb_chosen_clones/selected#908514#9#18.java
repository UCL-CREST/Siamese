    public void test1() throws Exception {
        String senha = "minhaSenha";
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(senha.getBytes());
        byte[] bytes = digest.digest();
        BASE64Encoder encoder = new BASE64Encoder();
        String senhaCodificada = encoder.encode(bytes);
        System.out.println("Senha     : " + senha);
        System.out.println("Senha SHA1: " + senhaCodificada);
    }
