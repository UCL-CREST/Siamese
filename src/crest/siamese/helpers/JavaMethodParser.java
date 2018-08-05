package crest.siamese.helpers;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.PrettyPrinter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import crest.siamese.document.Method;
import crest.siamese.settings.Settings;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.*;

public class JavaMethodParser implements MethodParser {
    private ArrayList<Method> methodList = new ArrayList<Method>();
    private String FILE_PATH = "";
    private String PREFIX_TO_REMOVE = "";
    private String MODE = Settings.MethodParserType.METHOD;
    private static String JAVA_PACKAGE = "";
    private static String JAVA_CLASS = "";
    private boolean isPrint;
    private String license = "none";

    public JavaMethodParser() {
        super();
    }

    public JavaMethodParser(String filePath, String prefixToRemove, String mode, boolean isPrint) {
        super();
        FILE_PATH = filePath;
        PREFIX_TO_REMOVE = prefixToRemove;
        MODE = mode;
        this.isPrint = isPrint;
    }

    @Override
    public void configure(String filePath, String prefixToRemove, String mode, boolean isPrint) {
        FILE_PATH = filePath;
        PREFIX_TO_REMOVE = prefixToRemove;
        MODE = mode;
        this.isPrint = isPrint;
    }

    public void setFILE_PATH(String FILE_PATH) {
        this.FILE_PATH = FILE_PATH;
    }

    public void setPREFIX_TO_REMOVE(String PREFIX_TO_REMOVE) {
        this.PREFIX_TO_REMOVE = PREFIX_TO_REMOVE;
    }

    public void setMODE(String MODE) {
        this.MODE = MODE;
    }

    public void setPrint(boolean print) {
        isPrint = print;
    }

    public String getLicense() {
        return license;
    }

    /***
     * Extract both methods and constructors
     * @return a list of methods & constructors
     */
    @Override
    public ArrayList<Method> parseMethods() {
        try {
            FileInputStream in = new FileInputStream(FILE_PATH);
            CompilationUnit cu;

            // method-level parser
            if (MODE.equals(Settings.MethodParserType.METHOD)) {
                try {
                    cu = JavaParser.parse(in);
                    Optional comment = cu.getComment();
                    if (comment.isPresent()) {
                        String headerComment = comment.get().toString();
                        license = LicenseExtractor.extractLicenseWithRegExp(headerComment);
                    } else {
                        license = "none";
                    }
                    NodeList<TypeDeclaration<?>> types = cu.getTypes();
                    for (TypeDeclaration type : types) {
                        if (type instanceof ClassOrInterfaceDeclaration) {

                            // getting class name
                            ClassOrInterfaceDeclaration classDec = (ClassOrInterfaceDeclaration) type;
                            JAVA_CLASS = classDec.getName().asString();
                        }
                    }

                    new MethodVisitor().visit(cu, null);
                    new ConstructorVisitor().visit(cu, null);

                } catch (Throwable e) {
                    if (isPrint)
                        System.out.println("Unparseable method (use whole fragment)");
                    Method m = getWholeFragment();
                    if (m != null)
                        methodList.add(m);
                } finally {
                    in.close();
                }
            } else {
                // file-level parser
                Method m = getWholeFragment();
                if (m != null)
                    methodList.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return methodList;
    }

    private Method getWholeFragment() throws FileNotFoundException {
        try {
            File f = new File(FILE_PATH);
            InputStream fStream = FileUtils.openInputStream(f);
            String content = org.apache.commons.io.IOUtils.toString(fStream, "UTF-8");
            int lines = content.split("\r\n|\r|\n").length;

            Method m = new Method(
                    FILE_PATH,
                    "package",
                    "ClassName",
                    "method",
                    "",
                    content,
                    1,
                    lines,
                    new LinkedList<crest.siamese.document.Parameter>(),
                    "");
            return m;
        } catch (NoSuchElementException e) {
            System.out.println("ERROR: can't parse + get whole fragment from file " + FILE_PATH);
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            System.out.println("ERROR: couldn't find the file " + FILE_PATH);
            e.printStackTrace();
            return null;
        }
    }

    private String getOnlyMethodName(String methodHeader) {
        String[] methodNames = methodHeader.split(" ");
        String methodName = "no_name";
        for (String mName : methodNames) {
            if (mName.contains("(")) {
                methodName = mName.substring(0, mName.indexOf("("));
            }
        }
        return methodName;
    }

    /***
     * Extract methods
     */
    private class MethodVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(MethodDeclaration n, Object arg) {
            // TODO: Do we need this?
//            List<Parameter> parameterArrayList = n.getParameters();
            ArrayList<crest.siamese.document.Parameter> paramsList = new ArrayList<>();
//            for (Parameter p: parameterArrayList) {
//                paramsList.add(
//                        new crest.siamese.document.Parameter(
//                                p.getType().toString(),
//                                p.getNameAsString()));
//            }

            // do not include comments in the indexed code
            PrettyPrinterConfiguration ppc = new PrettyPrinterConfiguration();
            ppc.setPrintComments(false);
            ppc.setPrintJavaDoc(false);
            // retrieve the comments separately
            String comment = retrieveComments(n);
            int begin = -1;
            int end = -1;
            if (n.getBegin().isPresent()) begin = n.getBegin().get().line;
            if (n.getEnd().isPresent()) end = n.getEnd().get().line;
            methodList.add(createNewMethod(n.getName().asString(), comment, n.toString(ppc), begin,
                    end, paramsList, n.getDeclarationAsString()));
            super.visit(n, arg);
        }
    }

    /***
     * Extract constructors
     */
    private class ConstructorVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(ConstructorDeclaration c, Object arg) {

//            List<Parameter> parameterArrayList = c.getParameters();
            ArrayList<crest.siamese.document.Parameter> paramsList = new ArrayList<>();
//            for (Parameter p: parameterArrayList) {
//                paramsList.add(
//                        new crest.siamese.document.Parameter(
//                                p.getType().toString(),
//                                p.getNameAsString()));
//            }

            // do not include comments in the indexed code
            PrettyPrinterConfiguration ppc = new PrettyPrinterConfiguration();
            ppc.setPrintComments(false);
            ppc.setPrintJavaDoc(false);
            String comment = retrieveComments(c);
            int begin = -1;
            int end = -1;
            if (c.getBegin().isPresent()) begin = c.getBegin().get().line;
            if (c.getEnd().isPresent()) end = c.getEnd().get().line;
            methodList.add(createNewMethod(c.getName().asString(), comment, c.toString(ppc), begin,
                    end, paramsList, c.getDeclarationAsString()));
            super.visit(c, arg);
        }
    }

    private String concatComments(List<Comment> comments) {
        String com = "";
        for (Comment c: comments) {
            com += "/*" + c.getContent() + "*/";
        }
        return com;
    }

    private String retrieveComments(Node n) {
        // retrieve the comments separately
        String commentStr;
        List<Comment> comments = n.getAllContainedComments();
        commentStr = concatComments(comments);
        commentStr += concatComments(n.getOrphanComments());
        if (n.getComment().isPresent()) {
            commentStr += "/**" + n.getComment().get().getContent() + "*/";
        }
        return commentStr;
    }

    private Method createNewMethod(String name, String comment, String src, int begin, int end,
                                   ArrayList<crest.siamese.document.Parameter> paramsList, String declaration) {
        Method m = new Method(
                FILE_PATH
                , JAVA_PACKAGE, JAVA_CLASS, name, comment, src, begin, end, paramsList, declaration);
        return m;
    }

    public void printMethods(String javaFile) throws IOException {
        FileInputStream in = new FileInputStream(javaFile);
        CompilationUnit cu;

        cu = JavaParser.parse(in);
        new MethodVisitor().visit(cu, null);
        new ConstructorVisitor().visit(cu, null);
        in.close();
    }
}
