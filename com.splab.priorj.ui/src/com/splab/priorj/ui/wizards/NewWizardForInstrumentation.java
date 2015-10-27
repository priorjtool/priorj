package com.splab.priorj.ui.wizards;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

import com.edu.ufcg.splab.priorj.controllers.PriorJServices;
import com.splab.priorj.ui.PriorJUI;
import com.splab.priorj.ui.PriorJUILog;
import com.splab.priorj.ui.models.InstrumentationManager;

/**
 * Wizard for new Instrumentation.
 *  
 * @author Samuel T. C. Santos
 *
 */
public class NewWizardForInstrumentation extends Wizard implements INewWizard {

	private static final String ORIGINAL_PROJECT_TAG = "_Old";
	private SelectionInstrumentationWizardPage instrumentationPage;
	
	public NewWizardForInstrumentation() {
		
		IDialogSettings instrumentationSettings = PriorJUI.getDefault().getDialogSettings();

		IDialogSettings wizardSettings = instrumentationSettings.getSection("instrumentationPageWizard");

		if (wizardSettings == null )
			wizardSettings = instrumentationSettings.addNewSection("instrumentationPageWizard");
			
		setDialogSettings(instrumentationSettings);
	}
	
	public void addPages() {
		setWindowTitle("New Instrumentation");
		instrumentationPage = new SelectionInstrumentationWizardPage();
		addPage(instrumentationPage);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean performFinish() {
		final String project = instrumentationPage.getProjectName();
		final String version = instrumentationPage.getVersionName();
		final String oldProject = instrumentationPage.getOldProjectName();
				
		try {
			getContainer().run(true, true, new IRunnableWithProgress(){
				
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
				{
					performOperation(project,oldProject, version, monitor);
				}
			});
		}
		catch (InvocationTargetException e){
			PriorJUILog.logError(e);
			return false;
		}
		catch (InterruptedException e){
			//User canceled, so stop but don't close wizard.
			return false;
		}
		
		return true;
	}
	
	private void performOperation(String project, String oldProject, String version, IProgressMonitor monitor) throws InterruptedException {
		// TODO Auto-generated method stub
		//Storage this information,because will be retrieve later by prioritization wizard.
		InstrumentationManager manager = InstrumentationManager.getInstance();
		manager.saveInPreferences(version, project);
		
		PriorJServices services = PriorJServices.getInstance();
		String basepath = services.readLocalBase();
		if (!basepath.equals("default")){
			try {
				services.setLocalBasePath(basepath);
				//Task 1
				monitor.beginTask("Instrumenting the Project",IProgressMonitor.UNKNOWN); 
				boolean toDo = true;
				while (toDo) {
					services.cloneProject(project, version);
					services.refreshProject(version);
					
					if (!oldProject.isEmpty()){
						services.cloneProject(oldProject, version+ORIGINAL_PROJECT_TAG);
						services.refreshProject(version+ORIGINAL_PROJECT_TAG);	
						services.checkDiffs(version, version+ORIGINAL_PROJECT_TAG);		
					}
					toDo = false; 
					if (monitor.isCanceled()) 
						 throw new InterruptedException("Canceled by user");
					 monitor.worked(1);   
				 }
								
			 	 monitor.done();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				PriorJUILog.logError(e);
			}
		}
	}

}
