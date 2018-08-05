    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String senha = "";
        String email = request.getParameter("EmailLogin");
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(request.getParameter("SenhaLogin").getBytes(), 0, request.getParameter("SenhaLogin").length());
            senha = new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Usuario usuario = UsuarioBll.getUsuarioByEmailAndSenha(email, senha);
        String redirect = request.getHeader("REFERER").replace("?msg=3", "").replace("&msg=3", "") + "?&msg=3";
        if (request.getHeader("REFERER").indexOf("?") != -1) {
            redirect = request.getHeader("REFERER").replace("?msg=3", "").replace("&msg=3", "") + "&msg=3";
        }
        if (usuario.getNome() != null) {
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);
            redirect = "index.jsp";
        }
        response.sendRedirect(redirect);
    }
