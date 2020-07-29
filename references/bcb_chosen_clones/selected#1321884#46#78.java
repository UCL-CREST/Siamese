    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nomeDoArquivoZip = request.getParameter("filename");
        int idSessao = Integer.parseInt(request.getParameter("idsessao"));
        String loginUsuario = request.getParameter("login");
        List<String> listaDeNomesDosArquivos = filesManager.getFilesNames(idSessao);
        byte b[] = new byte[1024];
        try {
            OutputStream out = response.getOutputStream();
            ZipOutputStream zout = new ZipOutputStream(out);
            response.setHeader("Content-Disposition", "attachment;filename=" + nomeDoArquivoZip);
            response.setContentType("application/zip");
            for (int i = 0; i < listaDeNomesDosArquivos.size(); i++) {
                String nomeArquivoAtual = listaDeNomesDosArquivos.get(i);
                if (nomeArquivoAtual != null) {
                    File arquivo = new File(DEFAULT_PATH + FILE_SEPARATOR + idSessao + "-" + loginUsuario + FILE_SEPARATOR + nomeArquivoAtual);
                    System.out.println(DEFAULT_PATH + FILE_SEPARATOR + idSessao + "-" + loginUsuario + FILE_SEPARATOR + nomeArquivoAtual);
                    InputStream in = new ByteArrayInputStream(getArrayBytes(arquivo));
                    ZipEntry e = new ZipEntry(nomeArquivoAtual);
                    zout.putNextEntry(e);
                    int len = 0;
                    while ((len = in.read(b)) != -1) {
                        zout.write(b, 0, len);
                    }
                }
                zout.closeEntry();
            }
            zout.close();
            out.close();
        } catch (IOException ex) {
            System.err.println("Erro no Download do arquivo: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
