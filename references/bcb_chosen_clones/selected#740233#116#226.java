    private void generateSchema() {
        ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance().getConsole();
        MessagesWindowDockable mconsole = (MessagesWindowDockable) console.getDockableById(MessagesWindowDockable.DOCKABLE_ID);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        IDocumentContainer container = (IDocumentContainer) DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer().getCurrentDockable();
        try {
            SchemaGenerationModel model = view.getModel();
            boolean isValid = transformationIsValid(model);
            if (!isValid) {
                return;
            }
            DefaultXPontusWindowImpl.getInstance().getStatusBar().setMessage("Generating schema...");
            view.setVisible(false);
            InputFormat inFormat = null;
            OutputFormat of = null;
            if (model.getInputType().equalsIgnoreCase("RELAX NG Grammar")) {
                inFormat = new SAXParseInputFormat();
            } else if (model.getInputType().equalsIgnoreCase("RELAX NG Compact Grammar")) {
                inFormat = new CompactParseInputFormat();
            } else if (model.getInputType().equalsIgnoreCase("DTD")) {
                inFormat = new DtdInputFormat();
            } else if (model.getInputType().equalsIgnoreCase("XML")) {
                inFormat = new XmlInputFormat();
            }
            if (model.getOutputType().equalsIgnoreCase("DTD")) {
                of = new DtdOutputFormat();
            } else if (model.getOutputType().equalsIgnoreCase("Relax NG Grammar")) {
                of = new RngOutputFormat();
            } else if (model.getOutputType().equalsIgnoreCase("XML Schema")) {
                of = new XsdOutputFormat();
            } else if (model.getOutputType().equalsIgnoreCase("Relax NG Compact Grammar")) {
                of = new RncOutputFormat();
            }
            ErrorHandlerImpl eh = new ErrorHandlerImpl(bao);
            SchemaCollection sc = null;
            if (!view.getModel().isUseExternalDocument()) {
                JTextComponent jtc = DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer().getCurrentEditor();
                if (jtc == null) {
                    XPontusComponentsUtils.showErrorMessage("No document opened!!!");
                    DefaultXPontusWindowImpl.getInstance().getStatusBar().setMessage("Error generating schema, Please see the messages window!");
                    return;
                }
                String suffixe = model.getOutputType().toLowerCase();
                File tmp = File.createTempFile("schemageneratorhandler", +System.currentTimeMillis() + "." + suffixe);
                OutputStream m_outputStream = new FileOutputStream(tmp);
                CharsetDetector detector = new CharsetDetector();
                detector.setText(jtc.getText().getBytes());
                Writer m_writer = new OutputStreamWriter(m_outputStream, "UTF-8");
                IOUtils.copy(detector.detect().getReader(), m_writer);
                IOUtils.closeQuietly(m_writer);
                try {
                    sc = inFormat.load(UriOrFile.toUri(tmp.getAbsolutePath()), new String[0], model.getOutputType().toLowerCase(), eh);
                } catch (Exception ife) {
                    ife.printStackTrace();
                    StrBuilder stb = new StrBuilder();
                    stb.append("\nError loading input document!\n");
                    stb.append("Maybe the input type is invalid?\n");
                    stb.append("Please check again the input type list or trying validating your document\n");
                    throw new Exception(stb.toString());
                }
                tmp.deleteOnExit();
            } else {
                try {
                    sc = inFormat.load(UriOrFile.toUri(view.getModel().getInputURI()), new String[0], model.getOutputType().toLowerCase(), eh);
                } catch (Exception ife) {
                    StrBuilder stb = new StrBuilder();
                    stb.append("\nError loading input document!\n");
                    stb.append("Maybe the input type is invalid?\n");
                    stb.append("Please check again the input type list or trying validating your document\n");
                    throw new Exception(stb.toString());
                }
            }
            OutputDirectory od = new LocalOutputDirectory(sc.getMainUri(), new File(view.getModel().getOutputURI()), model.getOutputType().toLowerCase(), DEFAULT_OUTPUT_ENCODING, DEFAULT_LINE_LENGTH, DEFAULT_INDENT);
            of.output(sc, od, new String[0], model.getInputType().toLowerCase(), eh);
            mconsole.println("Schema generated sucessfully!");
            DefaultXPontusWindowImpl.getInstance().getStatusBar().setMessage("Schema generated sucessfully!");
            if (model.isOpenInEditor()) {
                XPontusComponentsUtils.showWarningMessage("The document will NOT be opened in the editor sorry for that!\n You need to open it yourself.");
            }
        } catch (Exception ex) {
            DefaultXPontusWindowImpl.getInstance().getStatusBar().setMessage("Error generating schema, Please see the messages window!");
            StringWriter sw = new StringWriter();
            PrintWriter ps = new PrintWriter(sw);
            ex.printStackTrace(ps);
            StrBuilder sb = new StrBuilder();
            sb.append("Error generating schema");
            sb.appendNewLine();
            sb.append(new String(bao.toByteArray()));
            sb.appendNewLine();
            if (ex instanceof SAXParseException) {
                SAXParseException spe = (SAXParseException) ex;
                sb.append("Error around line " + spe.getLineNumber());
                sb.append(", column " + spe.getColumnNumber());
                sb.appendNewLine();
            }
            sb.append(sw.toString());
            mconsole.println(sb.toString(), OutputDockable.RED_STYLE);
            logger.error(sb.toString());
            try {
                ps.flush();
                ps.close();
                sw.flush();
                sw.close();
            } catch (IOException ioe) {
                logger.error(ioe.getMessage());
            }
        } finally {
            console.setFocus(MessagesWindowDockable.DOCKABLE_ID);
            Toolkit.getDefaultToolkit().beep();
        }
    }
