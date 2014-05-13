package com.cloudera.sa.aws.s3.upload;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
//import com.amazonaws.event.ProgressListener;


/**
 * Quick Java App to upload to s3 using InputStream
 */
public class App 
{
    public static void main( String[] args ) 
    {
    	
    	//setup
    	String filePath = "";
    	//args
    	if (args.length == 1) {
    		filePath = args[0];
    	}
    	else {
    		System.out.println("enter file path");
    		System.exit(1);
    	}
    	String fileName = filePath.substring(filePath.lastIndexOf('/')+1);
    	//Integer statsInterval = 5000;
   
    	String bucketName = "pebert-s3upload";
    	String key = fileName;
    	InputStream input = null;
		try {
			input = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
    	ObjectMetadata objectMetadata = new ObjectMetadata();
    	
    	//file length
    	objectMetadata.setContentLength(new File(filePath).length());
    	
    	//Credentials
    	//DefaultAWSCredentialsProviderChain credentialProviderChain = new DefaultAWSCredentialsProviderChain();
    	AWSCredentials AWSCred = new BasicAWSCredentials("accesskey",
    			"secretkey");
    	
    	TransferManager tx = new TransferManager(AWSCred);
    	
    	System.out.println( "starting upload" );
    	Upload s3Upload = tx.upload(bucketName, key, input, objectMetadata);
    	
    	//quick hack to start tracking when transfer is in progress
    	while(s3Upload.getState() == Transfer.TransferState.Waiting) {
    	}
    	System.out.println( "upload in progress" );
    	Long startTime = System.currentTimeMillis();
    	
		// optional asynchronous notifications about your transfer's progress.
    	//ProgressListener myProgressListener;
    	//s3Upload.addProgressListener(myProgressListener);
    	 
    	// block the current thread and wait for your transfer to complete
    	// AmazonClientException or AmazonServiceException detailing the reason
    	try {
			s3Upload.waitForCompletion();
		} catch (AmazonServiceException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (AmazonClientException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(1);
		}
    	
    	//calc b/s
    	Double BpS = (double)s3Upload.getProgress().getTotalBytesToTransfer() / (System.currentTimeMillis() - startTime) * 1000;
    	double seconds = ((double)System.currentTimeMillis() - startTime) / 1000;
    	System.out.format("Total transfer speed: %.0f Bps%n", BpS);
    	System.out.format("Transfer time: %.1f sec%n", seconds);
    	
    	// After the upload is complete, call shutdownNow to release the resources.
    	tx.shutdownNow();
    	
    	System.out.println( "File transfer complete." );
    }
}
