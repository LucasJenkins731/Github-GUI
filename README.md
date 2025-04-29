# Github-GUI
GitHub-GUI allows a user to create a Git repo and push it to right to GitHub.

## Getting started
In the terminal, the user must provide their username and token. Users must provide this information to continue to use the GUI. Without any user info, the GUI will not function as intended.

# Working with the GUI

## Inputs
Under the logo, the user will enter their inputs into the text box. The buttons at the bottom require input from the user to work.

## Buttons
The GUI houses multiple buttons that allow the user to create and use their repo. Each button has its own use that will help the user create and use their repo efficiently and effectively.

![Image](https://github.com/user-attachments/assets/f716a698-cffa-49ec-b859-5f2f448456e2)

### Repo-ify
Creates the initial repo in the user's computer. The user will input the absolute file path where they want their local repo to be created. This acts as "git init".

### Mirror
Creates a repo in GitHub that the user will be able to connect to. In the text box, the user will input information for the repo they are going to make. The users input will be as follows: "name for new repo"/"description of repo"/"is the repo private (yes/no)"/"file path of local repo to be connected". Note that when the user is actually doing this, their inputs will not have quotation marks in them, just slashes separating their text. Note: if the user successfully used Repo-ify earlier, they will not need to include the "local repo path to be connected" portion, since Repo-ify copies and stores it.

This acts as creating a repo on GitHub and then connecting the local repo to the repo on GitHub (git remote add origin "connection link").

Example input for the Mirror function (in the textfield):
MyRepoName/This is the description for my GitHub repository!/yes

Link to a GitHub repo made with this program: https://github.com/WilsonC67/FolderProject4

### Initial commit
Creates an initial commit to the repo that is instantly pushed to GitHub. This acts as multiple commands being used at the same time. First, all files are added to the repo (git add .). Then a commit is created with the message "Initial commit" (git commit -m "Initial commit"). Finally, the commit is pushed into the master branch of the repo (git push origin master).

*Disclaimer: This application is solely a prototype and not yet meant for commercial use.*

Sources and More Information about the Referenced Libraries used in this program:
https://csc109.github.io/GitSubprocessClient/
https://csc109.github.io/GitSubprocessClient/methods-overview

https://csc109.github.io/GitHubApiClient/
https://csc109.github.io/GitHubApiClient/methods-overview

How to create your GitHub token:
https://csc109.github.io/GitHubApiClient/access-token