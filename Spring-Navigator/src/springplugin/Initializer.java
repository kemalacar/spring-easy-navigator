package springplugin;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.PlatformUI;

public class Initializer implements IStartup {

	public void earlyStartup() {

		Job j = new Job("Setting spring data...") {

			@Override
			protected IStatus run(IProgressMonitor arg0) {
				try {

					PluginController.getInstance().initialize();

				} catch (Exception e) {
					e.printStackTrace();
				}

				return Status.OK_STATUS;
			}
		};

		j.setUser(false);
		j.schedule();

	}

	public void earlyStartup2() {

		Runnable r = new Runnable() {

			@Override
			public void run() {

				try {
					PluginController.getInstance().initialize();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

		r.run();

	}

}
