    public void preparaEscrita(DadosSeriais dados, String dir) throws BasicException {
        String idArquivo = dados.getIdColeta().toString();
        if (dados.getIdResumo() != null) {
            idArquivo += "_" + dados.getIdResumo().toString();
        }
        if (dir == null || dir.trim().equals("")) {
            throw BasicException.errorHandling("Diret�rio Imagem de Dados n�o foi definido.", "msgErroNaoExisteDiretorioImagem", new String[] {}, log);
        }
        try {
            fos = new FileOutputStream(new File(dir.trim(), "pdump." + idArquivo + ".data.xml.zip"));
        } catch (FileNotFoundException e) {
            throw BasicException.errorHandling("Erro ao abrir arquivo de dados xml para escrita", "msgErroCriarArquivoDadosXML", e, log);
        }
        zipOutputStream = new ZipOutputStream(fos);
        zipOutputStream.setLevel(Deflater.DEFAULT_COMPRESSION);
        try {
            zipOutputStream.putNextEntry(new ZipEntry("pdump." + idArquivo + ".data.xml"));
            xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(zipOutputStream, ENCODING);
        } catch (Exception e) {
            throw BasicException.errorHandling("Erro ao criar entrada em arquivo compactado", "msgErroCriarEntradaZipArquivoDadosXML", e, log);
        }
        XMLStreamWriter osw = null;
        try {
            xmlWriter = XMLOutputFactory.newInstance().createXMLStreamWriter(zipOutputStream, ENCODING);
        } catch (Exception e) {
            throw BasicException.errorHandling("Erro ao criar entrada em arquivo compactado", "msgErroCriarEntradaZipArquivoDadosXML", e, log);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        if (dados.getTipos().size() > 0) {
            tipos = new int[dados.getTipos().size()];
            for (int c = 0; c < dados.getTipos().size(); c++) {
                String tipo = dados.getTipos().get(c);
                if (tipo.equals(LIT_INTEIRO)) {
                    tipos[c] = INTEIRO;
                } else if (tipo.equals(LIT_DECIMAL)) {
                    tipos[c] = DECIMAL;
                } else if (tipo.equals(LIT_DATA)) {
                    tipos[c] = DATA;
                } else {
                    tipos[c] = TEXTO;
                }
            }
        }
        try {
            xmlWriter.writeStartDocument(ENCODING, "1.0");
            xmlWriter.writeStartElement("coleta");
            xmlWriter.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            xmlWriter.writeAttribute("xsi:noNamespaceSchemaLocation", "http://www.pingifes.lcc.ufmg.br/dtd/pingifes_dados_seriais.xsd");
            xmlWriter.writeStartElement("id");
            xmlWriter.writeCharacters(idArquivo);
            xmlWriter.writeEndElement();
            xmlWriter.writeStartElement("data");
            xmlWriter.writeCharacters(dateFormat.format(dados.getDataColeta()));
            xmlWriter.writeEndElement();
            xmlWriter.writeStartElement("sql");
            xmlWriter.writeCData(dados.getSql());
            xmlWriter.writeEndElement();
            xmlWriter.writeStartElement("cabecalho");
            for (int c = 0; c < dados.getCabecalho().size(); c++) {
                xmlWriter.writeStartElement("nome");
                xmlWriter.writeAttribute("tipo", dados.getTipos().get(c));
                xmlWriter.writeCharacters(dados.getCabecalho().get(c));
                xmlWriter.writeEndElement();
            }
            xmlWriter.writeEndElement();
            xmlWriter.writeStartElement("quantidade");
            xmlWriter.writeCharacters("" + dados.getQuantidade());
            xmlWriter.writeEndElement();
            xmlWriter.writeStartElement("dados");
        } catch (XMLStreamException e) {
            throw BasicException.errorHandling("Erro ao escrever dados em arquivo compactado", "msgErroEscreverArquivoDadosXML", e, log);
        }
        calculadorHash = new CalculaHash();
    }
