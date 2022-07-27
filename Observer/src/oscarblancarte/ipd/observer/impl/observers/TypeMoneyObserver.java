package oscarblancarte.ipd.observer.impl.observers;



import java.text.NumberFormat;

import oscarblancarte.ipd.observer.impl.ConfigurationManager;
import oscarblancarte.ipd.observer.impl.IObserver;

public class TypeMoneyObserver implements IObserver{

	@Override
	public void notifyObserver(String command, Object source) {
		 if(command.equals("typeMoneyFormat")){
	            ConfigurationManager conf = (ConfigurationManager)source;
	            System.out.println("Observer ==> TypeMoneyObserver.typeMoneyFormat > " 
	                    + NumberFormat.getCurrencyInstance(conf.getTypemoney()).format(1.11));
	        }
		
	}

}
