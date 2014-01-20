package test;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;

import java.io.FileInputStream;
import java.util.List;

import springplugin.utils.PluginUtil;


public class ParseEx {

	
	public static void main(String[] args) throws Exception {
        // creates an input stream for the file to be parsed
		String s ="C:\\Users\\EXT0173773\\runtime-EclipseApplication\\SINGLE_HUB_WEB\\src\\main\\java\\com\\turkcell\\singlehub\\web\\controller\\common\\ErrorController.java";
//        FileInputStream in = new FileInputStream(s);
//
//        CompilationUnit cu;
//        try {
//            // parse the file
//            cu = JavaParser.parse(in);
//        } finally {
//            in.close();
//        }
        
        // prints the resulting compilation unit to default system output

        
        PluginUtil.controllerHasGivenAnnotation(s, "RequestMapping", PluginUtil.truncateUrl("/site/hata/404"));
	}
	
	private static void changeMethods(CompilationUnit cu) {
        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
        	System.out.println(type);
        	
        	type.getAnnotations();
        	
        	
            List<BodyDeclaration> members = type.getMembers();
            for (BodyDeclaration member : members) {
                if (member instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) member;
                    System.out.println(method);
                }
            }
        }
    }
}
