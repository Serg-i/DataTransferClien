package validation;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import main.Window;

public class PortVerifier extends InputVerifier {

	@Override
	public boolean verify(JComponent input) {
		String portStr = ((JTextField) input).getText();
		try{
			int port = Integer.parseInt(portStr);
			if ((port>1024)&&(port <65535)){
				return true;
			}
			return false;
		}catch(NumberFormatException e){
			Window.log("\n wrong port number \n");
			Window.blockButton();
			return false;
		}
	}

}
