package gui;

import java.util.Locale;
import javax.swing.UIManager;
import businessLogic.BLFacade;
import businessLogic.BLFactory;
import configuration.ConfigXML;


public class ApplicationLauncher { 
	
	public static void main(String[] args) {

		ConfigXML c=ConfigXML.getInstance();
		System.out.println(c.getLocale());
		Locale.setDefault(new Locale(c.getLocale()));
		System.out.println("Locale: "+Locale.getDefault());
		
	    LoginGUI a = new LoginGUI();
		a.setVisible(true);
		
		try {	
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			
			BLFacade appFacadeInterface;
			appFacadeInterface = BLFactory.getBusinessLogicFactory(c.isBusinessLogicLocal());
            LoginGUI.setBussinessLogic(appFacadeInterface);
			
		}catch (Exception e) {
			System.out.println("Error in ApplicationLauncher: "+e.toString());
		}
	}
}
