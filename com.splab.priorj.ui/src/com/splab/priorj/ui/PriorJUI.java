package com.splab.priorj.ui;

import java.io.File;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Version;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.splab.priorj.ui.models.PrioritizationManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class PriorJUI extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.splab.priorj.ui"; //$NON-NLS-1$

	// The shared instance
	private static PriorJUI plugin;
		
	private IEclipsePreferences configPrefs;


	/**
	 * The constructor
	 */
	public PriorJUI() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		PrioritizationManager manager = PrioritizationManager.getManager();
		manager.load();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		saveConfigPrefs();
		PrioritizationManager manager = PrioritizationManager.getManager();
		manager.save();
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static PriorJUI getDefault() {
		return plugin;
	}
	/**
	 * Accessing configuration location
	 * 
	 * @return
	 */
	public File getConfigDir(){
		Location location = Platform.getConfigurationLocation();
		
		if (location != null){
			URL configURL = location.getURL();
			if (configURL != null && configURL.getProtocol().startsWith("file")){
				return new File(configURL.getFile(), PLUGIN_ID);
			}
		}
		//If the configuration directory is read-only
		//then return an alternate location
		// rather than null or throwing an Exception
		return getStateLocation().toFile();
	}
	
	public Preferences getConfigPrefs(){
		if (configPrefs == null){
			configPrefs = new ConfigurationScope().getNode(PLUGIN_ID);
		}
		return configPrefs;
	}

	public void saveConfigPrefs(){
		if (configPrefs != null){
			try{
				configPrefs.flush();
			}
			catch(BackingStoreException e){
				e.printStackTrace();
			}
		}
	}
	
	public Version getVersion(){
		return new Version((String) getBundle().getHeaders().get(org.osgi.framework.Constants.BUNDLE_VERSION));
	}


	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
