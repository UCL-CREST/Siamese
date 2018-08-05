    public Download gerarArquivo(final Date datIni, final Date datFim) {
        validator.checking(new Validations() {

            {
                that(datIni != null, "lancamentoContabil.datIni", "obrigatorio", i18n("lancamentoContabil.datIni"));
                that(datFim != null, "lancamentoContabil.datFim", "obrigatorio", i18n("lancamentoContabil.datFim"));
            }
        });
        validator.onErrorUse(Results.page()).of(this.getClass()).exibir();
        List<LancamentoContabil> lancamentoList = dao.list(datIni, datFim);
        try {
            File arq = new File("Lancamento.txt");
            PrintWriter pw = new PrintWriter(arq);
            StringBuffer texto;
            for (LancamentoContabil lanc : lancamentoList) {
                texto = new StringBuffer();
                texto.append("\"\",\"");
                texto.append(lanc.getContaDebito() + "\",\"");
                texto.append(lanc.getContaCredito() + "\",\"");
                texto.append(formataData(lanc.getData()) + "\",\"");
                texto.append((lanc.getCredtio() != null ? formataValor(lanc.getCredtio()) : formataValor(lanc.getDebito())) + "\",\"");
                texto.append("\",\"");
                texto.append("\",\"");
                texto.append((lanc.getNomHistorico() == null ? "" : lanc.getNomHistorico()) + "\",\"");
                texto.append(lanc.getBoleto().getNumDocumento() + "\"");
                pw.println(texto.toString());
            }
            pw.close();
            FileInputStream fis = new FileInputStream(arq);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead = 0;
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            byte[] b = baos.toByteArray();
            InputStream input = new ByteArrayInputStream(b);
            result.include("sucess", "Arquivo Gerado com sucesso!");
            return new InputStreamDownload(input, "application/txt", "Lancamento.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
