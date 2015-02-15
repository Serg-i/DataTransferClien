package validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import main.Window;

public class AddressVerifier extends InputVerifier {

	@Override
	public boolean verify(JComponent input) {
		String ValidIpAddressRegex = 
				"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
				"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		Pattern ipPattern = Pattern.compile(ValidIpAddressRegex);
		String portStr = ((JTextField) input).getText();
		Matcher match = ipPattern.matcher(portStr);
		if(match.matches()){
			return true;
		}
		Window.blockButton();
		Window.log("wrong ip address \n");
		return false;
	}

}
