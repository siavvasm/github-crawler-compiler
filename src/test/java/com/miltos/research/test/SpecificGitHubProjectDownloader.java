package com.miltos.research.test;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;

import com.miltos.research.apps.GitCloner;

public class SpecificGitHubProjectDownloader {

	public static void main(String[] args) {
		
		// 0. Construct the GitCloner for a specific project
		GitCloner gitCloner = new GitCloner();
		GitCloner.setRemotePath("clone https://github.com/libgdx/libgdx");
		
		// 1. Initialize the folder in which it will be cloned
		try {
			gitCloner.init(1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("**********************************************************");
			e.printStackTrace();
		}
		
		// 2. Clone the desired software repository
		try {
			gitCloner.testClone();
		} catch (IOException | GitAPIException e) {
			// TODO Auto-generated catch block
			System.out.println("-----------------------------------------------------------");
			e.printStackTrace();
		}

	}

}
