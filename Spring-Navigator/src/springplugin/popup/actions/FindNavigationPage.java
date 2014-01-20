package springplugin.popup.actions;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import springplugin.PluginController;

public class FindNavigationPage implements IEditorActionDelegate {

	private Shell shell;
	private IEditorPart targetPart;

	/**
	 * Constructor for Action1.
	 */
	public FindNavigationPage() {
		super();
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		shell = targetPart.getSite().getShell();
	}

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {


		//redirect:/sepetim/odeme-bilgileri ise controllerda request mappingi-sepetim/odeme-bilgileri- olan metoda git.
		//
		//		 IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		//		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorSite().getKeyBindingService();

		ISelectionProvider provider  = targetPart.getEditorSite().getSelectionProvider();

		//		IWorkbench workbench = PlatformUI.getWorkbench();
		//		IWorkbenchWindow window = 
		//		        workbench == null ? null : workbench.getActiveWorkbenchWindow();
		//		IWorkbenchPage activePage = 
		//		        window == null ? null : window.getActivePage();
		//
		//		IEditorPart editor = 
		//		        activePage == null ? null : activePage.getActiveEditor();
		//		IEditorInput input = 
		//		        editor == null ? null : editor.getEditorInput();
		//		
		//		IPath path = input instanceof FileEditorInput 
		//		        ? ((FileEditorInput)input).getPath()
		//		        : null;

		if(provider.getSelection() instanceof ITextSelection){

			try {
				
				ITextSelection selection = (ITextSelection)provider.getSelection();
				System.out.println(selection.getText());
				
				if(!selection.getText().isEmpty()){
					PluginController.getInstance().searchForSelectedFromJavaFile(targetPart,selection.getText());
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			


		}

	}

	/**
	 * @see IActionDelegate#selectionChanged(IAction, ISelection)
	 */
	public void selectionChanged(IAction arg0, ISelection arg1) {

		//		System.out.println("selection chang " + arg1);

	};
	@Override
	public void setActiveEditor(IAction arg0, IEditorPart editor) {
		System.out.println("setActiveEditor");
		this.targetPart = editor;

	}

}
