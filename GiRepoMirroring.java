//make imports
import github.tools.client.GitHubApiClient;
import github.tools.client.RequestParams;
import git.tools.client.GitSubprocessClient;
import github.tools.client.GitHubApiClient;
import github.tools.responseObjects.*;


import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class GiRepoMirroring {
    
    public static void main (String[] args){

        Scanner scan= new Scanner(System.in);


        //getting information to make the repo
            System.out.print("Enter your GitHub username: ");
            String username = scan.nextLine();

            System.out.print("Enter the name of the reposiory: ");
            String nameOfRepo = scan.nextLine();
            

            System.out.print("Enter the description of your repository (optional): ");
            String descripOfRepo = scan.nextLine();

            System.out.print("Is your repository private? (Yes or No): ");
            String privRepoStr = scan.nextLine().toUpperCase();

            boolean privRepo = privRepoStr.equalsIgnoreCase("YES");

            System.out.println("Enter your GitHub access token: ");
            String token = scan.nextLine();
            System.out.print(token);

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
            GitSubprocessClient gitSubprocessClient = new GitSubprocessClient(localRepoPath);

    
         //create gihub repo

         try{
            String gitRemoteAdd = gitSubprocessClient.gitRemoteAdd("origin", createRepoResponse.getUrl());
            } catch (Exception e) {
                System.err.println("Error during GitHub mirroring: " + e.getMessage());
                e.printStackTrace();
            }

        System.out.println("Mirroring to GitHub complete. Check your repository at: " + createRepoResponse.getUrl());
    }

}
