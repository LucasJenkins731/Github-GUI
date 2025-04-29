package src;
import javax.swing.*;

import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import github.tools.responseObjects.*;
import github.tools.client.RequestParams;
import github.tools.client.GitHubApiClient;
import git.tools.client.GitSubprocessClient;

// the line "myLabel.setText("<html>"+ myString +"</html>")" and all similar lines were taken/evolved upon from the below URL:
// https://stackoverflow.com/questions/2420742/make-a-jlabel-wrap-its-text-by-setting-a-max-width

public class Main {
    JFrame frame;
    JPanel panel, userPanel, buttonPanel;
	String username, token;
	RepoNameHolder repoNameHolder;

    public Main(String username, String token) throws IOException {
		this.username = username;
		this.token = token;
		GitHubApiClient gitHubApiClient = new GitHubApiClient(username, token);
		
		frame = new JFrame();
		
		
        BufferedImage image = ImageIO.read(new File("QUxMS Logo.png"));
		ImageIcon logo = new ImageIcon(image);
		JLabel logoPicture = new JLabel(logo);


		JButton button = new JButton();
		JLabel label = new JLabel("This will display the text in response to the buttons you press.");
		JTextField textField = new JTextField("Please enter your input and then click on its corresponding button to submit.");
		repoNameHolder = new RepoNameHolder(" ");

		ButtonListener buttonListener = new ButtonListener(button, label, textField, repoNameHolder, username, token);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(actionedButton("Repo-ify", "Make sure that the absolute path is not enclosed within quotation marks.", buttonListener));
		buttonPanel.add(actionedButton("Mirror", "Enter your desired repo name, description, and Yes/No for privacy, all " + 
											"separated by a slash (/) and press this button.", buttonListener));
        buttonPanel.add(actionedButton("Initial Commit", "This instantly adds, commits, and pushes to GitHub!", buttonListener));

		/* Unused buttons
			buttonPanel.add(actionedButton("Credentials", "", buttonListener));
			buttonPanel.add(actionedButton("Add", " ", buttonListener));
			buttonPanel.add(actionedButton("Commit", "Simply write your commit message in the text field.", buttonListener));
		*/

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

	public String getUsername() {
		return username;
	}
	
	public String getToken() {
		return token;
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
				
			}

		} catch(IllegalArgumentException e) {
            System.out.println("Wrong type of argument.");
        }

        new Main(username, token);
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
		private JTextField textField;
		private String username, token;
		RepoNameHolder repoNameHolder;
		GitHubApiClient gitHubApiClient;


		public ButtonListener(JButton button, JLabel label, JTextField textField, RepoNameHolder repoNameHolder, String username, String token) {
			this.button = button;
			this.label = label;
			this.textField = textField;
			this.repoNameHolder = new RepoNameHolder(" ");
			this.username = username;
			this.token = token;
		}

		public String getUsername() {
			return username;
		}
		
		public String getToken() {
			return token;
		}

		@Override
		public void actionPerformed(ActionEvent e) {

			switch(e.getActionCommand()) {
				
				case "Repo-ify":

				// Turn a project/directory on a user's computer into a Git repo
				String repoPath = textField.getText();
				repoNameHolder.setText(repoPath);
				GitSubprocessClient gitSubprocessClient = new GitSubprocessClient(repoNameHolder.getRepoName());
				String gitInit = gitSubprocessClient.gitInit();	
				label.setText("<html>"+ gitInit + "</html>");
				break;
				
				// case "Add":
				// System.out.println("Add button pressed.");
				// break;
				
				// Adds all changed files, commits, and pushes to Github as the initial commit.
                case "Initial Commit":

				GitSubprocessClient gitSubprocessClient2 = new GitSubprocessClient(repoNameHolder.getRepoName());
				String gitAddAll = gitSubprocessClient2.gitAddAll();
				String commit = gitSubprocessClient2.gitCommit("Initial commit");
				String push = gitSubprocessClient2.gitPush("master");
				label.setText("Initial commit has been created.");
				break;

				// case "Commit":
				// GitSubprocessClient gitSubprocessClient2 = new GitSubprocessClient(repoNameHolder.getRepoName());
				// String commitMessage = textField.getText();
				// String commit2 = gitSubprocessClient2.gitCommit(commitMessage);
				// label.setText("<html>"+ commit2 + "</html>");
				// break;
				
				// Creates a repo on GitHub
				case "Mirror":

				GitHubApiClient gitHubApiClient = new GitHubApiClient(getUsername(), getToken());

				String userInput = textField.getText();
				String[] userInfo = userInput.split("/");
				for (String s : userInfo) {
					System.out.println(s);
				}

                String nameOfRepo = userInfo[0];

                String descriptionRepo = userInfo[1];

                String privateRepoBoolean = userInfo[2];

                String localRepoPath = repoNameHolder.getRepoName();

                boolean privateRepo = privateRepoBoolean.equalsIgnoreCase("YES");
				
	
				// get the path to the repo
				// System.out.println("Enter the path to your local git repository and press the mirror button.");
				// String localRepoPath = textField.getText();

				File localRepo = new File(localRepoPath);
				if (!localRepo.exists() || !localRepo.isDirectory() || !new File(localRepo, ".git").exists()){
					System.err.println("Error: Invalid local repository path. Please re-enter and make sure you are entering a valid Git repository directory.");
					return;
				}
	
				//creating the repo in Github
				RequestParams requestParams = new RequestParams();
				requestParams.addParam("name", nameOfRepo); // name of repo
				requestParams.addParam("description", descriptionRepo); // repo description
				requestParams.addParam("private", privateRepoBoolean); // if the repo is private/public

				CreateRepoResponse createRepoResponse = gitHubApiClient.createRepo(requestParams);
	
				// setting the repo to origin
				GitSubprocessClient gitSubprocessClient3 = new GitSubprocessClient(localRepoPath);
	
			 	// create github repo
	
				try {
					String gitRemoteAdd = gitSubprocessClient3.gitRemoteAdd("origin", createRepoResponse.getUrl());
					} catch (Exception ex) {
						System.err.println("Error during GitHub mirroring: " + ex.getMessage());
						ex.printStackTrace();
					}
	
				label.setText("Mirroring to GitHub complete. Check your repository at: " + createRepoResponse.getUrl());
			}
		}
	}
}	