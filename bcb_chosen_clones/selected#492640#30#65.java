    @BeforeClass
    public static void createProblem() throws IOException, ParserConfigurationException, TransformerException {
        problem = File.createTempFile("___prb___", ".problem");
        System.out.println("created temporary problem " + problem);
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(problem));
        PrintStream ps = new PrintStream(zos);
        zos.putNextEntry(new ZipEntry("MANIFEST"));
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = builderFactory.newDocumentBuilder();
        Document doc = builder.newDocument();
        Element root = doc.createElement("resources");
        doc.appendChild(root);
        Element alias = doc.createElement("alias");
        alias.setAttribute("path", "STATEMENT");
        alias.setAttribute("target", "statement.txt");
        root.appendChild(alias);
        Element contents1 = doc.createElement("contents");
        contents1.setAttribute("path", "ANSWER");
        contents1.setTextContent("42");
        root.appendChild(contents1);
        Element contents2 = doc.createElement("contents");
        contents2.setAttribute("path", "STATEMENT EXT");
        contents2.setTextContent("TXT");
        root.appendChild(contents2);
        Element teacher = doc.createElement("teacher");
        teacher.setAttribute("path", "ANSWER");
        root.appendChild(teacher);
        Source source = new DOMSource(doc);
        Result result = new StreamResult(zos);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(source, result);
        zos.putNextEntry(new ZipEntry("statement.txt"));
        ps.println("���� ����� ������������ ����� � ����?");
        zos.closeEntry();
        zos.close();
    }
