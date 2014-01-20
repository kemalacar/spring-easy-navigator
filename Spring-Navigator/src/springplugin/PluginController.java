package springplugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;
import org.xml.sax.SAXException;

import springplugin.utils.MethodInfo;
import springplugin.utils.PluginUtil;

public class PluginController {

	private static boolean INITIALIZATION_OK=false;
	private static PluginController instance =null;


	private Map<String, ArrayList<String>> tileXmlFiles=null;
	private Map<String, ArrayList<String>> controllers=null;
	private Map<String, ArrayList<String>> jspFiles=null;


	public static PluginController getInstance(){
		if(instance==null)
			instance = new PluginController();

		return instance;
	}

	private PluginController() {
		init();
	}

	public void initialize() throws Exception{

		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

		for (IProject iProject : projects) {
			if(iProject.isOpen()){
				checkTilesDefinitionXmlFiles(iProject);
			}
		}
		INITIALIZATION_OK=true;
	}

	public  void lookInTileXmls(){

	}

	public void searchForSelectedFromJavaFile(IEditorPart targetPart, String searchText) throws Exception{

		if(searchText.startsWith("redirect:")){
			searchText = searchText.replace("redirect:", "");
		}
		if(INITIALIZATION_OK){
			FileEditorInput input = 	(FileEditorInput) targetPart.getEditorSite().getPage().getNavigationHistory().getCurrentLocation().getInput();
			IProject project =  input.getFile().getProject();

			searchInXml(project,searchText,targetPart);
		}else{
			MessageDialog.openError(targetPart.getSite().getShell(), "Please Wait", "Please wait for initialization.");
		}


	}

	private void init(){

		if(tileXmlFiles==null){
			tileXmlFiles =   new HashMap<String, ArrayList<String>>();
		}

		if(controllers==null){
			controllers =   new HashMap<String, ArrayList<String>>();
		}

		if(jspFiles==null){
			jspFiles =   new HashMap<String, ArrayList<String>>();
		}
	}

	private void searchInXml(IProject project, String searchText, IEditorPart targetPart) throws ParserConfigurationException, SAXException, IOException{

		//TODO sirasini ogren... once pathe gore mi bakiyor yoksa once controllerlara mi?
		boolean continueSearch=true;
		String targetPath="";

		ArrayList<String> jFiels = jspFiles.get(project.getName());
		if(searchText.contains("/")){
			for (String jf : jFiels) {
				if(jf.replace(".jsp", "").endsWith(searchText.replace("/", "\\"))){
					targetPath = jf;
					break;
				}
			}	
		}

		if(targetPath.equals("") ){
			ArrayList<String> defFiles = tileXmlFiles.get(project.getName());

			if(defFiles!=null && defFiles.size()>0){

				for (String fPath : defFiles) {
					String retval = PluginUtil.getTargetFileBYDefinitionName(new File(fPath), searchText);
					if(!"".equals(retval)){
						targetPath = getJspFilePath(jFiels,retval);
						break;
					}
				}
			}
		}
		MethodInfo mInfo=null;
		if(targetPath.equals("") ){
			ArrayList<String> cntrlls = controllers.get(project.getName());
			if(cntrlls!=null && cntrlls.size()>0){
				for (String c : cntrlls) {
					//					System.out.println(c);
					mInfo= PluginUtil.controllerHasGivenAnnotation(c, "RequestMapping", PluginUtil.truncateUrl(searchText)) ;
					if(mInfo!=null) {
						targetPath = c;
						break;
					}
				}
			}
		}
		
		System.out.println(targetPath);
		if(targetPath!="" ){
			//			if( targetPath.endsWith(".java"))
			//			{
			openInIde( targetPath,targetPart, mInfo);
			//			}else{
			//			}
		}else{
			MessageDialog.openError(PlatformUI.getWorkbench().getDisplay().getActiveShell(), "Not Found", "Page Could not found");
		}

	}

	private String getJspFilePath(ArrayList<String> jFiels, String jspName){

		for (String jsp : jFiels) {
			if(jsp.endsWith(jspName.replace("/", "\\"))){

				return jsp;
			}

		}
		return "";
	}

	private void checkTilesDefinitionXmlFiles(IProject project) throws Exception{

		//		ArrayList<String> list = getProjectSourceFoldersName(project);
		ArrayList<String> foundedXml =  new ArrayList<String>();
		ArrayList<String> foundedControllers =  new ArrayList<String>();
		ArrayList<String> foundedJsp=  new ArrayList<String>();

		FileWalker.findAllXmlAndJavaFilesUnderFolder(project.getLocationURI().getPath(),foundedXml,foundedControllers,foundedJsp);

		System.out.println("xml:"+ foundedXml.size()  + " controllers: "+foundedControllers.size());

		controllers.put(project.getName(), foundedControllers);
		tileXmlFiles.put(project.getName(), foundedXml);
		jspFiles.put(project.getName(), foundedJsp);
	}


	public ArrayList<String> getProjectSourceFoldersName(IProject project) throws JavaModelException{
		ArrayList<String> srcNames = new ArrayList<String>();
		if (project.isOpen() && JavaProject.hasJavaNature(project)) 
		{ 
			IJavaProject javaProject = JavaCore.create(project);

			IPackageFragmentRoot[] packageFragmentRoot = javaProject.getAllPackageFragmentRoots();
			for (int i = 0; i < packageFragmentRoot.length; i++){
				if (packageFragmentRoot[i].getElementType() == IJavaElement.PACKAGE_FRAGMENT_ROOT && !packageFragmentRoot[i].isArchive())
					srcNames.add(packageFragmentRoot[i].getResource().getLocation().toOSString());
			}

		}

		return srcNames;

	}
	
//	private void openInIde(IProject project , String selectedFile, IEditorPart targetPart){
//		
//		IPath path= new Path(selectedFile);
////		IFile iFile = project.getFile(path);
//
//		try {
//			
//			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
//			IResource res = root.getContainerForLocation(path) ;
//			
//			 HashMap map = new HashMap();
//			 IMarker marker = res.createMarker(IMarker.TEXT);
//			 map.put(IMarker.LINE_NUMBER, new Integer(5));
//			 map.put(IWorkbenchPage.EDITOR_ID_ATTR,"org.eclipse.ui.DefaultTextEditor");
//			 marker.setAttributes(map);
//			 IDE.openEditor(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage(), marker); //3.0 API
//			 marker.delete();
//			 
//		} catch (CoreException e) {
//			e.printStackTrace();
//		}
//		
//	
//	}

	private void openInIde(String selectedFile,IEditorPart targetPart, MethodInfo mInfo){
		

		IPath path= new Path(selectedFile);
		IFileStore fileStore = EFS.getLocalFileSystem().getStore(path);
		
		File file=new File(selectedFile);
		if(file.isFile()){
			try {
				IEditorPart part = IDE.openEditorOnFileStore( Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage(), fileStore );
				
				if(selectedFile.endsWith(".java") && part instanceof ITextEditor){
					 ITextEditor editor = (ITextEditor) part;
					 IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
					 boolean selectedOk=false;
					 for (int i = 1; i <5; i++) {//check if  method head 1-5 lines
						 IRegion lineInfo = document.getLineInformation(mInfo.getLine()-i);//
						 String lineStr=  document.get(lineInfo.getOffset(), lineInfo.getLength());
						 
						 if(lineStr.indexOf(mInfo.getName())>0){
							 editor.selectAndReveal(lineInfo.getOffset()+lineStr.indexOf(mInfo.getName()),mInfo.getName().length());
							 selectedOk=true;
						 }
					}
					 
					 if(!selectedOk){
						 IRegion lineInfo = document.getLineInformation(mInfo.getLine()-3);
						 editor.selectAndReveal(lineInfo.getOffset(),0);
					 }
					 
					 
					 //					 editor.setHighlightRange(7*25,45 , true);
				}

			}catch (Exception e) {
				MessageDialog.openError(Activator.getDefault().getWorkbench().getDisplay().getActiveShell(), "Error",e.getLocalizedMessage());
			}
		}
	}


}
