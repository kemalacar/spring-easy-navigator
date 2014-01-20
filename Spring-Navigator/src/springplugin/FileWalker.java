package springplugin;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.File;
import java.util.ArrayList;

import springplugin.utils.PluginUtil;



public class FileWalker {


	public static void main(String[] args) throws Exception {

		//		String rootFolder="C:\\Users\\EXT0173773\\workspace3\\SpringPlugin";
		//		List<FileElement> founded =	findAllXmlAndJavaFilesUnderFolder(rootFolder);

		//		for (FileElement string : founded) {
		//			if(string.endsWith(".java")){
		//				File f= new File(string);
		//				try {
		//
		//					String className=f.getPath().replace(rootFolder, "").replace("\\", ".").replace(".java", "");
		//					if(className.startsWith(".")){
		//						className = className.substring(1);
		//					}
		//					className = className.replace("src.", "");
		//
		//					System.out.println(className);
		//
		//					Class.forName(className).getAnnotations();
		//
		//				} catch (ClassNotFoundException e) {
		//					// TODO Auto-generated catch block
		//					e.printStackTrace();
		//				}	
		//			}	
		//		}
	}


	public static void findAllXmlAndJavaFilesUnderFolder(String path, ArrayList<String> foundedXml, ArrayList<String> foundedControllers, ArrayList<String> foundedJsp) throws Exception {

		listFolders(new File(path),foundedXml,foundedControllers,foundedJsp);		
	}

	private static void listFolders(File root, ArrayList<String> foundedXml, ArrayList<String> foundedControllers, ArrayList<String> foundedJsp) throws Exception{
		for (File file : root.listFiles()) {
			if( !file.isDirectory()){
				if(file.getName().endsWith(".xml") ){
					try {
						if(PluginUtil.isTilesFile(file)){
							foundedXml.add(file.getPath());
						}
					} catch (Exception e) {
						continue;
					}
				}else if( file.getName().endsWith(".java") ){
					try{
						CompilationUnit compUnit = JavaParser.parse(file);
						if(PluginUtil.findAnnotationFromJava(compUnit,"Controller")){
							foundedControllers.add(file.getPath());
						}
					}catch (Exception e){ 
						continue;
					}
				}else if( file.getName().endsWith(".jsp") ){
					foundedJsp.add(file.getPath());
				}

			}else if( file.isDirectory()){
				listFolders(file,foundedXml,foundedControllers,foundedJsp) ;
			}
		}
	}


	//	private static boolean isValidFile(File file, ArrayList<String> srcFolders){
	//		if(file.getName().endsWith(".xml") ){
	//			return true;
	//		}else if( file.getName().endsWith(".java") ){
	//
	//			for (String src : srcFolders) {
	//				if(file.getPath().startsWith(src)){
	//
	//					String s = file.getPath().replace(src, "").replace(".java", "").replace("\\", ".");
	//					if(s.startsWith("."))
	//						s=s.substring(1);
	//
	//					System.out.println(s);
	//
	//					return true;
	//				}
	//			}
	//
	//		}
	//
	//		return false;
	//	}



}
