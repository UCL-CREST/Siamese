    public void invoke(MessageContext msgContext) throws AxisFault {
        log.debug("Enter: MD5AttachHandler::invoke");
        try {
            Message msg = msgContext.getRequestMessage();
            SOAPConstants soapConstants = msgContext.getSOAPConstants();
            org.apache.axis.message.SOAPEnvelope env = (org.apache.axis.message.SOAPEnvelope) msg.getSOAPEnvelope();
            org.apache.axis.message.SOAPBodyElement sbe = env.getFirstBody();
            org.w3c.dom.Element sbElement = sbe.getAsDOM();
            org.w3c.dom.Node n = sbElement.getFirstChild();
            for (; n != null && !(n instanceof org.w3c.dom.Element); n = n.getNextSibling()) ;
            org.w3c.dom.Element paramElement = (org.w3c.dom.Element) n;
            String href = paramElement.getAttribute(soapConstants.getAttrHref());
            org.apache.axis.Part ap = msg.getAttachmentsImpl().getAttachmentByReference(href);
            javax.activation.DataHandler dh = org.apache.axis.attachments.AttachmentUtils.getActivationDataHandler(ap);
            org.w3c.dom.Node timeNode = paramElement.getFirstChild();
            long startTime = -1;
            if (timeNode != null && timeNode instanceof org.w3c.dom.Text) {
                String startTimeStr = ((org.w3c.dom.Text) timeNode).getData();
                startTime = Long.parseLong(startTimeStr);
            }
            long receivedTime = System.currentTimeMillis();
            long elapsedTime = -1;
            if (startTime > 0) elapsedTime = receivedTime - startTime;
            String elapsedTimeStr = elapsedTime + "";
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            java.io.InputStream attachmentStream = dh.getInputStream();
            int bread = 0;
            byte[] buf = new byte[64 * 1024];
            do {
                bread = attachmentStream.read(buf);
                if (bread > 0) {
                    md.update(buf, 0, bread);
                }
            } while (bread > -1);
            attachmentStream.close();
            buf = null;
            String contentType = dh.getContentType();
            if (contentType != null && contentType.length() != 0) {
                md.update(contentType.getBytes("US-ASCII"));
            }
            sbe = env.getFirstBody();
            sbElement = sbe.getAsDOM();
            n = sbElement.getFirstChild();
            for (; n != null && !(n instanceof org.w3c.dom.Element); n = n.getNextSibling()) ;
            paramElement = (org.w3c.dom.Element) n;
            String MD5String = org.apache.axis.encoding.Base64.encode(md.digest());
            String senddata = " elapsedTime=" + elapsedTimeStr + " MD5=" + MD5String;
            paramElement.appendChild(paramElement.getOwnerDocument().createTextNode(senddata));
            sbe = new org.apache.axis.message.SOAPBodyElement(sbElement);
            env.clearBody();
            env.addBodyElement(sbe);
            msg = new Message(env);
            msgContext.setResponseMessage(msg);
        } catch (Exception e) {
            log.error(Messages.getMessage("exception00"), e);
            throw AxisFault.makeFault(e);
        }
        log.debug("Exit: MD5AttachHandler::invoke");
    }
