    private void mandaMail(String sTo, Session se) {
        state("Preparando envio para: '" + sTo + "'");
        try {
            txtDe.setVlrString(buscaEmailFilial());
            MimeMessage mes = new MimeMessage(se);
            mes.setFrom(new InternetAddress(txtDe.getVlrString()));
            mes.setSubject(txtAssunto.getVlrString());
            mes.setSentDate(new Date());
            mes.addRecipient(RecipientType.TO, new InternetAddress(sTo));
            BodyPart parte = new MimeBodyPart();
            parte.setText(txaMen.getVlrString());
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(parte);
            String sTextoAdic = "";
            try {
                Date datacompilacao = SystemFunctions.getClassDateCompilation(this.getClass());
                sTextoAdic += "Vers�o:" + Funcoes.dateToStrDataHora(datacompilacao) + "\n";
            } catch (Exception e) {
                e.printStackTrace();
            }
            sTextoAdic += "\nMensagem:\n" + txaMen.getVlrString();
            parte.setText(sTextoAdic);
            if (fArq != null) {
                parte = new MimeBodyPart();
                FileDataSource orig = new FileDataSource(fArq);
                parte.setDataHandler(new DataHandler(orig));
                parte.setFileName(fArq.getName());
                parte.setDisposition(Part.ATTACHMENT);
                multipart.addBodyPart(parte);
            }
            mes.setContent(multipart);
            state("Enviando dados...");
            Transport.send(mes);
            state("Envio OK...");
        } catch (MessagingException err) {
            Funcoes.mensagemErro(this, "Erro ao enviar mensagem para: " + sTo + "\n" + err.getMessage());
            err.getStackTrace();
            state("Aguardando reenvio.");
        }
    }
