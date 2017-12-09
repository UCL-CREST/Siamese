package crest.siamese.helpers;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
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
    public static String JAVA_PACKAGE = "";
    public static String JAVA_CLASS = "";
    private boolean isPrint;

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
            if (MODE == Settings.MethodParserType.METHOD) {
                try {
                    cu = JavaParser.parse(in);

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
                    FILE_PATH.replace(PREFIX_TO_REMOVE, ""),
                    "package",
                    "ClassName",
                    "method",
                    content
                    .replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)",""),
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

            List<Parameter> parameterArrayList = n.getParameters();
            ArrayList<crest.siamese.document.Parameter> paramsList = new ArrayList<>();
            for (Parameter p: parameterArrayList) {
                paramsList.add(
                        new crest.siamese.document.Parameter(
                                p.getType().toString(),
                                p.getNameAsString()));
            }

            // do not include comments in the indexed code
            PrettyPrinterConfiguration ppc = new PrettyPrinterConfiguration();
            ppc.setPrintComments(false);

            Method m = new Method(
                    FILE_PATH.replace(PREFIX_TO_REMOVE, "")
                    , JAVA_PACKAGE
                    , JAVA_CLASS
                    , n.getName().asString()
                    , n.toString(ppc)
                    , n.getBegin().get().line
                    , n.getEnd().get().line
                    , paramsList
                    , n.getDeclarationAsString());
            methodList.add(m);
            super.visit(n, arg);
        }
    }

    /***
     * Extract constructors
     */
    private class ConstructorVisitor extends VoidVisitorAdapter {
        @Override
        public void visit(ConstructorDeclaration c, Object arg) {

            List<Parameter> parameterArrayList = c.getParameters();
            ArrayList<crest.siamese.document.Parameter> paramsList = new ArrayList<>();
            for (Parameter p: parameterArrayList) {
                paramsList.add(
                        new crest.siamese.document.Parameter(
                                p.getType().toString(),
                                p.getNameAsString()));
            }

            // do not include comments in the indexed code
            PrettyPrinterConfiguration ppc = new PrettyPrinterConfiguration();
            ppc.setPrintComments(false);

            Method m = new Method(
                    FILE_PATH.replace(PREFIX_TO_REMOVE, "")
                    , JAVA_PACKAGE
                    , JAVA_CLASS
                    , c.getName().asString()
                    , c.toString(ppc)
                    , c.getBegin().get().line
                    , c.getEnd().get().line
                    , paramsList
                    , c.getDeclarationAsString());
            methodList.add(m);
            super.visit(c, arg);
        }
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
