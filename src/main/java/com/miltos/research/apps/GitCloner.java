package com.miltos.research.apps;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

/**
 * This class is responsible for executing Git commands.
 * @author Miltos
 *
 */
//TODO: Reduce the number of the init() methods
public class GitCloner {
	
	// Basic Paths
	public static String BASE_DIR = new File(System.getProperty("user.dir")).getAbsolutePath();
	public static String GIT_REPO = new File(BASE_DIR + "/Benchmark_Repo").getAbsolutePath();
	public static String PROJECT_PATH = "";
	
	// Member Variables
	private static String localPath, remotePath, email;
    private static Repository localRepo;
	private static Git git;
	 
	/*
	 * Constructors...
	 */
	public GitCloner(){

		// Create the git repository 
		File gitRepo = new File(GIT_REPO);
		gitRepo.mkdir(); 
	 }
	
	public GitCloner(String remote){
		 this.remotePath = remote;
		
		// Create the git repository 
    	File gitRepo = new File(GIT_REPO);
    	gitRepo.mkdir();
		 
	 }
	
	public GitCloner(String git, String remote){
		// Create the git repository 
		File gitRepo = new File(git);
		gitRepo.mkdir(); 
	 }

	/*
	 * Setters and Getters...
	 */
	
	public static String getLocalPath() {
		return localPath;
	}

	public static void setLocalPath(String localPath) {
		GitCloner.localPath = localPath;
	}

	public static String getRemotePath() {
		return remotePath;
	}

	public static void setRemotePath(String remotePath) {
		GitCloner.remotePath = remotePath;
	}

	public static Repository getLocalRepo() {
		return localRepo;
	}

	public static void setLocalRepo(Repository localRepo) {
		GitCloner.localRepo = localRepo;
	}

	/**
	 * This method is responsible for creating the folder in which the specific 
	 * GitHub repository will be downloaded.
	 */
	public void init() throws IOException {
	    	
		// Create the Benchmark Repository in the predefined path
		File gitRepo = new File(GIT_REPO);
		gitRepo.mkdir();
	
		// Define the name of the folder in which the specific GitHub project will be cloned 
	    String folderName = new File(remotePath).getName();
	    String x = folderName.replace(".git", "");
	   
	    // Create the exact path of the project under evaluation
	    PROJECT_PATH = GIT_REPO +"/"+ x + "_" + email;
	    
	    // Create the folder in which the specific GitHub project will be cloned
		File f = new File(PROJECT_PATH);
	    if(f.exists()){
	    	FileUtils.deleteDirectory(f);
	    }
	    
	    // Clone the project (i.e. the remote repository)
	    localRepo = new FileRepository(PROJECT_PATH + "/.git");
	    git = new Git(localRepo);    

	    }
	
	/**
	 * This method is responsible for creating the folder in which the specific 
	 * GitHub repository will be downloaded.
	 */
	public void init(String gitPath) throws IOException {
    	
    	// Create the Benchmark Repository folder in the desired path
    	File gitRepo = new File(gitPath);
    	gitRepo.mkdir();
        
    	// Define the name of the folder in which the specific GitHub project will be cloned 
        String folderName = new File(remotePath).getName(); 
        String x = folderName.replace(".git", "");
       
        // Create the exact path of the project under evaluation
        PROJECT_PATH = GIT_REPO +"/"+ x + "_" + email;
        
        // Create the folder in which the specific GitHub project will be cloned
    	File f = new File(PROJECT_PATH);
        if(f.exists()){
        	FileUtils.deleteDirectory(f);
        }
        
        // Clone the project (i.e. the remote repository)
        localRepo = new FileRepository(PROJECT_PATH + "/.git");
        git = new Git(localRepo);    

    }
	
	/**
	 * This method is responsible for creating the folder in which the specific 
	 * GitHub repository will be downloaded.
	 */
	public void init(int index) throws IOException {
    	
		// Define the name of the folder in which the specific GitHub project will be cloned 
        String folderName = new File(remotePath).getName();
        folderName = index + "." + folderName;
        String x = folderName.replace(".git", "");
       
        // Create the exact path of the project under evaluation
        PROJECT_PATH = GIT_REPO +"/"+ x;
        
        // Create the folder in which the specific GitHub project will be cloned
    	File f = new File(PROJECT_PATH);
        if(f.exists()){
        	FileUtils.deleteDirectory(f);
        }
        
        // Clone the project (i.e. the remote repository)
        localRepo = new FileRepository(PROJECT_PATH + "/.git");
        git = new Git(localRepo);
        
    }
	
	/**
	 * This method is responsible for creating the folder in which the specific 
	 * GitHub repository will be downloaded.
	 */
	public void init(int index, String gitPath) throws IOException {
	    	
			// Define the name of the folder in which the specific GitHub project will be cloned 
	        String folderName = new File(remotePath).getName();
	        folderName = index + "." + folderName;
	        String x = folderName.replace(".git", "");
	       
	        // Create the exact path of the project under evaluation
	        PROJECT_PATH = gitPath +"/"+ x;
	        
	        System.out.println("Project path : " + PROJECT_PATH);
	        
	        // Create the folder in which the specific GitHub project will be cloned
	    	File f = new File(PROJECT_PATH);
	        if(f.exists()){
	        	FileUtils.deleteDirectory(f);
	        }
	        
	        // Clone the project (i.e. the remote repository)
	        localRepo = new FileRepository(PROJECT_PATH + "/.git");
	        git = new Git(localRepo);
	        
	
	    }
	
	public static void delete(File file)
		    	throws IOException{
		 
		    	if(file.isDirectory()){
		 
		    		//directory is empty, then delete it
		    		if(file.list().length==0){
		    			
		    		   file.delete();
		    		   System.out.println("Directory is deleted : " 
		                                                 + file.getAbsolutePath());
		    			
		    		}else{
		    			
		    		   //list all the directory contents
		        	   String files[] = file.list();
		     
		        	   for (String temp : files) {
		        	      //construct the file structure
		        	      File fileDelete = new File(file, temp);
		        		 
		        	      //recursive delete
		        	     delete(fileDelete);
		        	   }
		        		
		        	   //check the directory again, if empty then delete it
		        	   if(file.list().length==0){
		           	     file.delete();
		        	     System.out.println("Directory is deleted : " 
		                                                  + file.getAbsolutePath());
		        	   }
		    		}
		    		
		    	}else{
		    		//if file, then delete it
		    		file.delete();
		    		System.out.println("File is deleted : " + file.getAbsolutePath());
		    	}
		    }
		
    public void testCreate() throws IOException {
        Repository newRepo = new FileRepository(localPath + ".git");
        newRepo.create();
    }

    public void testClone() throws IOException, GitAPIException {
    	try {
    		Git.cloneRepository().setURI(remotePath)
                .setDirectory(new File(PROJECT_PATH)).call();
    	} catch (Exception e) {
    		System.out.println("An exception is avoided!");
    	}
    }
 
    public void testAdd() throws IOException, GitAPIException {
        File myfile = new File(localPath + "/myfile");
        myfile.createNewFile();
        git.add().addFilepattern("myfile").call();
    }

    public void testCommit() throws IOException, GitAPIException,
            JGitInternalException {
        git.commit().setMessage("Added myfile").call();
    }

    public void testPush() throws IOException, JGitInternalException,
            GitAPIException {
        git.push().call();
    }

    public void testTrackMaster() throws IOException, JGitInternalException,
            GitAPIException {
        git.branchCreate().setName("master")
                .setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
                .setStartPoint("origin/master").setForce(true).call();
    }

    public void testPull() throws IOException, GitAPIException {
        git.pull().call();
    }


}