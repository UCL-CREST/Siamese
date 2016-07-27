package elasticsearch;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import elasticsearch.document.Method;

public class MethodParser {
    public MethodParser() {

    }

	public ArrayList<Method> parseMethods(String filePath) {
		ArrayList<Method> methodList = new ArrayList<>();
		try {
			/* Parse and extract method body */
			FileInputStream in = new FileInputStream(filePath);
			CompilationUnit cu;
			try {
				// System.out.println("Parsing: " + filePath);
				// parse the file
				cu = JavaParser.parse(in);
				List<TypeDeclaration> typeDeclarations = cu.getTypes();
				for (TypeDeclaration typeDec : typeDeclarations) {
					List<BodyDeclaration> members = typeDec.getMembers();
					if (members != null) {
						for (BodyDeclaration member : members) {
							// extract the constructors
							if (member instanceof ConstructorDeclaration) {
								ConstructorDeclaration constructor = (ConstructorDeclaration) member;
								String cons = constructor.getDeclarationAsString() + constructor.getBlock();

                                // System.out.println(getOnlyMethodName(constructor.getDeclarationAsString()));

                                Method m = new Method(getOnlyMethodName(constructor.getDeclarationAsString()),cons);
								methodList.add(m);
							}
							// extract all the methods
							else if (member instanceof MethodDeclaration) {
								MethodDeclaration method = (MethodDeclaration) member;
								String mthd = method.getDeclarationAsString() + method.getBody().toString();

                                // System.out.println(getOnlyMethodName(method.getDeclarationAsString()));

                                Method m = new Method(getOnlyMethodName(method.getDeclarationAsString()), mthd);
								methodList.add(m);
							}
						}
					}
				}
			} catch (Throwable e) {
                // cannot parse, add the whole string = only 1 string return in the list
                StringBuilder builder = new StringBuilder();
                int ch;
                while((ch = in.read()) != -1){
                    builder.append((char)ch);
                }
                Method m = new Method("method", builder.toString());
                methodList.add(m);
            }
            finally {
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return methodList;
	}

	private String getOnlyMethodName(String methodHeader) {
        String[] methodNames = methodHeader.split(" ");
        String methodName = "no_name";
        for (String mName : methodNames) {
            if (mName.contains("("))
                methodName = mName.substring(0, mName.indexOf("("));
        }

        return methodName;
    }
}
