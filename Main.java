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
import github.tools.client.RequestParams;


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


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

				Scanner scan= new Scanner(System.in);
        


				//getting information to make the repo
		
					System.out.print("Enter the name of the reposiory: ");
					String nameOfRepo = scan.nextLine();
					
		
					System.out.print("Enter the description of your repository (optional): ");
					String descripOfRepo = scan.nextLine();
		
					System.out.print("Is your repository private? (Yes or No): ");
					String privRepoStr = scan.nextLine().toUpperCase();
		
					boolean privRepo = privRepoStr.equalsIgnoreCase("YES");
		
		
					GitHubApiClient gitHubApiClient = new GitHubApiClient(username, token);
		
		
					//get the path to the repo
					System.out.print("Enter the path to your local git repository: ");
					String localRepoPath =  scan.nextLine();
					File localRepo = new File(localRepoPath);
					if (!localRepo.exists() || !localRepo.isDirectory() || !new File(localRepo, ".git").exists()){
						System.err.println("Error: Invalid local respository path. Please re-enter and make sure you are entering a valid Git repository directory.");
						return;
					}
		
					//creating the repo in Github
					RequestParams requestParams = new RequestParams();
					requestParams.addParam("name", nameOfRepo); // name of repo
					CreateRepoResponse createRepoResponse = gitHubApiClient.createRepo(requestParams);
		
					//setting the repo to origin
					GitSubprocessClient gitSubprocessClient1 = new GitSubprocessClient(localRepoPath);
		
		
			
				 //create gihub repo
		
				String gitRemoteAdd = gitSubprocessClient1.gitRemoteAdd("origin", createRepoResponse.getUrl());
		
				System.out.println("Mirroring to GitHub complete. Check your repository at: " + createRepoResponse.getUrl());
			}
				
				
				System.out.println("Mirror button pressed.");
		}		
	}
}