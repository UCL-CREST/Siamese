package crest.isics.helpers;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.printer.PrettyPrinterConfiguration;
import crest.isics.document.Method;
import crest.isics.main.Experiment;
import crest.isics.settings.Settings;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class MethodParser {
    private ArrayList<Method> methodList = new ArrayList<Method>();
    private String FILE_PATH = "";
    private String PREFIX_TO_REMOVE = "";
    private String MODE = Settings.MethodParserType.METHOD;
    public static String JAVA_PACKAGE = "";
    public static String JAVA_CLASS = "";

    public MethodParser(String filePath, String prefixToRemove, String mode) {
        FILE_PATH = filePath;
        PREFIX_TO_REMOVE = prefixToRemove;
        MODE = mode;
    }

    /***
     * Extract both methods and constructors
     * @return a list of methods & constructors
     */
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
//                    System.out.println("File: " + FILE_PATH);
//                    System.out.println(e.getMessage());
                    if (Experiment.isPrint)
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
//            String content = new Scanner(new File(FILE_PATH)).useDelimiter("\\Z").next();
            Method m = new Method(
                    FILE_PATH.replace(PREFIX_TO_REMOVE, ""),
                    "package",
                    "ClassName",
                    "method",
                    content,
                    1,
                    lines,
                    new LinkedList<crest.isics.document.Parameter>(),
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
            ArrayList<crest.isics.document.Parameter> paramsList = new ArrayList<>();
            for (Parameter p: parameterArrayList) {
                paramsList.add(
                        new crest.isics.document.Parameter(
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
                    // copied the regex from
                    // http://stackoverflow.com/questions/9205988/writing-a-java-program-to-remove-the-comments-in-same-java-program
                    , n.toString(ppc)
//                    , n.toStringWithoutComments()
//                    .replaceAll("(?:/\\*(?:[^*]|(?:\\*+[^*/]))*\\*+/)|(?://.*)","")
                    , n.getBegin().get().line
//                    , n.getBeginLine()
//                    , n.getEndLine()
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
            ArrayList<crest.isics.document.Parameter> paramsList = new ArrayList<>();
            for (Parameter p: parameterArrayList) {
                paramsList.add(
                        new crest.isics.document.Parameter(
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
                    // copied the regex from
                    // http://stackoverflow.com/questions/9205988/writing-a-java-program-to-remove-the-comments-in-same-java-program
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
