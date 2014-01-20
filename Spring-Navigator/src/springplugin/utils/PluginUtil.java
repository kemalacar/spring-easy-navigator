package springplugin.utils;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.MemberValuePair;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import springplugin.exceptions.BreakSaxException;


public class PluginUtil {

	public static boolean findAnnotationFromJava(CompilationUnit cUnit,String annotation){
		for (TypeDeclaration type : cUnit.getTypes()) {

			if(type.getAnnotations()==null)
				continue;

			for (AnnotationExpr ann : type.getAnnotations()) {
				if(ann.getName().getName().equalsIgnoreCase(annotation)){
					return true;
				}
			}
		}
		return false;
	}

	public static MethodInfo controllerHasGivenAnnotation(String filePath, String annotation, String value){
		try {
			CompilationUnit compUnit = JavaParser.parse(new File(filePath));
			for (TypeDeclaration type : compUnit.getTypes()) {

				if(type.getMembers()==null)
					continue;

				for (BodyDeclaration member : type.getMembers()) {
					
					if(member.getAnnotations()==null || !(member instanceof MethodDeclaration))
						continue;
					
					MethodDeclaration method = (MethodDeclaration)member;
					
					for (AnnotationExpr mAnn : method.getAnnotations()) {
						if(mAnn.getName().getName().equalsIgnoreCase(annotation)  ){
							if(mAnn instanceof SingleMemberAnnotationExpr){
								SingleMemberAnnotationExpr sa = (SingleMemberAnnotationExpr) mAnn;
								
//								String s = PluginUtil.truncateUrl(sa.getMemberValue().toString());
								
								if(validateUrl(sa.getMemberValue().toString().replace("\"", ""), value))
									{
									return new MethodInfo(method.getName(), method.getBody().getBeginLine());
									}
								
//								if(s.equalsIgnoreCase(value)){
//									return true;
//								}
								
							}else if(mAnn instanceof NormalAnnotationExpr){
								NormalAnnotationExpr na = (NormalAnnotationExpr) mAnn;
								if(na.getPairs()==null || na.getPairs().size()==0)
									continue;

								for (MemberValuePair pair : na.getPairs()) {
									
									if(validateUrl(pair.getValue().toString().replace("\"", ""), value))
										{
										return new MethodInfo(method.getName(), method.getBody().getBeginLine());
										}
									
//									String s = PluginUtil.truncateUrl();
//									if(s.equalsIgnoreCase())
//										return true;
								}
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} 

		return null;
	}
	
	public static String truncateUrl(String str){
		
		if(str.startsWith("/")){
			str = str.substring(1);
		}
		
		if(str.endsWith("/")){
			str = str.substring(0, str.length()-1);
		}
		
		return str;
	}
	
	public static String getTargetFileBYDefinitionName(File xmlFile , String definitionName) throws ParserConfigurationException, SAXException, IOException{

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		MySaxHandler handler = new MySaxHandler();
		handler.setSearchVal(definitionName);
		try {
			saxParser.parse(xmlFile.getPath(),handler);
		} catch (BreakSaxException bs) {
			return handler.getRetVal();
		}
		return "";

	}


	public static boolean isTilesFile(File xmlFile ) throws ParserConfigurationException, SAXException, IOException{

		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser saxParser = factory.newSAXParser();

		MySaxHandler handler = new MySaxHandler();
		handler.setElementName("tiles-definitions");
		try {
			saxParser.parse(xmlFile.getPath(),handler);
		} catch (BreakSaxException bs) {
			return true;
		}
		return false;

	}
	
	public static boolean validateUrl(String reqMappingUrl, String searchUrl){
		reqMappingUrl = truncateUrl(reqMappingUrl);
			
		String[] reqArr = reqMappingUrl.split("/");
		String[] searchArr = searchUrl.split("/");
		boolean notSame=false;
		if(reqArr.length == searchArr.length){
			for (int i=0;i<reqArr.length;i++) {
				if(reqArr[i].startsWith("{") && reqArr[i].endsWith("}") 
//						|| searchArr[i].startsWith("${") && searchArr[i].endsWith("}")
						)
					continue;
				if(!reqArr[i].equals(searchArr[i])){
					notSame=true;
					return false;
				}
			}
		}else{
			notSame = true;
		}
		return !notSame;
	}

}


