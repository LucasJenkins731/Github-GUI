import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ButtonListener implements ActionListener{
	private JButton button;
	
	public ButtonListener(JButton button) {
		this.button = button;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "Username":
            break;

            case "Token":
            break;
            
            case "Repo-ify":
            break;
            
            case "Add":
            break;
            
            case "Commit":
            break;
            
            case "Mirror":
            break;
        }
    }
}