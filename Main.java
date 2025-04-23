import javax.swing.*;
import org.w3c.dom.Text;
import github.tools.client.GitHubApiClient;
import git.tools.client.GitSubprocessClient;
import github.tools.client.GitHubApiClient;
import github.tools.responseObjects.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// the line "myLabel.setText("<html>"+ myString +"</html>")" and all similar lines were taken/evolved upon from the below URL:
// https://stackoverflow.com/questions/2420742/make-a-jlabel-wrap-its-text-by-setting-a-max-width

public class Main {
    JFrame frame;
    JPanel panel, userPanel, buttonPanel;
	String username, token;
	RepoNameHolder repoNameHolder;

    public Main() throws IOException {
		frame = new JFrame();
		

        BufferedImage image = ImageIO.read(new File("QUxMS Logo.png"));
		ImageIcon logo = new ImageIcon(image);
		JLabel logoPicture = new JLabel(logo);


		JButton button = new JButton();
		JLabel label = new JLabel("This will display the text in response to the buttons you press.");
		JTextField textField = new JTextField("Please enter your input and then click on its corresponding button to submit.");
		repoNameHolder = new RepoNameHolder(" ");

		ButtonListener buttonListener = new ButtonListener(button, label, textField, repoNameHolder);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(actionedButton("Repo-ify", "Make sure that the absolute path is not enclosed within quotation marks.", buttonListener));
		buttonPanel.add(actionedButton("Add", " ", buttonListener));
		buttonPanel.add(actionedButton("Commit", "Simply write your commit message in the text field.", buttonListener));
		buttonPanel.add(actionedButton("Mirror", " ", buttonListener));
		//buttonPanel.add(actionedButton("Push", " ", buttonListener));
		//buttonPanel.add(actionedButton("Get Link", " ", buttonListener));

		userPanel = new JPanel();
		userPanel.setLayout(new BorderLayout());
		userPanel.add(textField, BorderLayout.NORTH);
		userPanel.add(buttonPanel, BorderLayout.SOUTH);
		userPanel.add(label, BorderLayout.CENTER);

		frame.setLayout(new BorderLayout());
		frame.add(logoPicture, BorderLayout.NORTH);
		frame.add(userPanel, BorderLayout.CENTER);
		frame.add(new JLabel("DISCLAIMER: This application is solely a prototype and not yet meant for commercial use."), 
								BorderLayout.SOUTH);

		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 600);
		frame.setLocationRelativeTo(null);
    }


    public static void main(String[] args) throws IOException {
		final String EMPTYSTRING = " ";
		String username = EMPTYSTRING;
		String token = EMPTYSTRING;

		try(Scanner userInput = new Scanner(System.in)) {
			while(username == EMPTYSTRING && token == EMPTYSTRING) {
				System.out.println("Please enter your GitHub username.");
				username = userInput.nextLine();

				System.out.println("Please enter your GitHub token.");
				token = userInput.nextLine();
				
				GitHubApiClient gitHubApiClient = new GitHubApiClient(username, token);
			}
		} catch(IllegalArgumentException e) {
            System.out.println("Wrong type of argument.");
        }

        new Main();
    }

	static JButton actionedButton(String text, String tooltip, ActionListener listener) {
		JButton button = new JButton();
		button.setText(text);
		button.setToolTipText(tooltip);
		button.addActionListener(listener);
		return button;
	}

	public class ButtonListener implements ActionListener {
		private JButton button;
		private JLabel label;
		RepoNameHolder repoNameHolder;
		private JTextField textField;

		public ButtonListener(JButton button, JLabel label, JTextField textField, RepoNameHolder repoNameHolder) {
			this.button = button;
			this.label = label;
			this.textField = textField;
			this.repoNameHolder = new RepoNameHolder(" ");
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {

			switch(e.getActionCommand()) {
				case "Repo-ify":
				// Turn a project on a user's computer into a Git repo
				String repoPath = textField.getText();
				repoNameHolder.setText(repoPath);
				GitSubprocessClient gitSubprocessClient = new GitSubprocessClient(repoNameHolder.getRepoName());
				String gitInit = gitSubprocessClient.gitInit();	
				label.setText("<html>"+ gitInit + "</html>");
				break;
				
				case "Add":
				System.out.println("Add button pressed.");
				break;
				
				case "Commit":
				GitSubprocessClient gitSubprocessClient2 = new GitSubprocessClient(repoNameHolder.getRepoName());
				String commitMessage = textField.getText();
				String commit = gitSubprocessClient2.gitCommit(commitMessage);
				label.setText("<html>"+ commit + "</html>");
				break;
				
				case "Mirror":
				System.out.println("Mirror button pressed.");
				break;

				/*
				 * Idea for pushing initial commit
				 * system.out.println("Pushing initial commit");
				 * String push = gitSubprocessClient.gitPush("master");
				 */

				/*
				 * Idea for URL
				 * system.out.println("Repo link: https://github.com/" + username + "/" + reponame);
				 */
			}
		}		
	}
}