package com.dreamferry.sugarmavenplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/** 
* Description: 
* @author 
* @version 1.0.0 2020年10月9日 下午3:48:05
*/
public abstract class AbstractSugarMojo extends AbstractMojo{

	public void execute() throws MojoExecutionException, MojoFailureException {
		executeProcess();
	}

	protected abstract void executeProcess() throws MojoExecutionException, MojoFailureException;
	
	protected void info(String message) {
		getLog().info("*** [sugar-maven-plugin] " + message + " ***");
	}
	protected void error(Exception e) {
		getLog().error(e);
	}
}
