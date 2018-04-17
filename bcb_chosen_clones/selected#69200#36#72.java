    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UsuarioBll usuarioBll = new UsuarioBll();
        String senha = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(request.getParameter("Senha").getBytes(), 0, request.getParameter("Senha").length());
            senha = new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        String[] data = request.getParameter("Nascimento").split("/");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(data[2]), Integer.parseInt(data[1]) - 1, Integer.parseInt(data[0]));
        Telefone telefone = new Telefone();
        Usuario usuario = new Usuario();
        usuario.setNome(request.getParameter("Nome"));
        telefone.setTelefone(request.getParameter("Telefone").replaceAll("\\D", ""));
        telefone.setTelefoneTipo(TelefoneTipoBll.getTelefoneTipoByTelefoneTipoID(Integer.parseInt(request.getParameter("TipoTelefone"))));
        usuario.setTelefone(telefone);
        usuario.setEmail(request.getParameter("Email"));
        usuario.setCpf(request.getParameter("CPF").replaceAll("\\D", ""));
        usuario.setNascimento(calendar);
        Endereco endereco = new Endereco();
        endereco.setCep(Integer.parseInt(request.getParameter("CEP").replaceAll("\\D", "")));
        endereco.setNumero(request.getParameter("Numero"));
        endereco.setComplemento(request.getParameter("Complemento"));
        usuario.setEndereco(endereco);
        usuario.setSenha(senha);
        String msg = "?msg=0";
        if (usuarioBll.addNewUsuario(usuario)) {
            msg = "?msg=1";
            Usuario usuarioAutenticado = UsuarioBll.getUsuarioByEmailAndSenha(usuario.getEmail(), usuario.getSenha());
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuarioAutenticado);
        }
        response.sendRedirect("templates/verde-rosa/cadastro.jsp" + msg);
    }
