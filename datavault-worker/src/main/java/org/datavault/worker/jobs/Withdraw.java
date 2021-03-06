package org.datavault.worker.jobs;

import java.util.Map;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.datavault.common.job.Context;
import org.datavault.common.job.Job;
import org.datavault.worker.queue.EventSender;
import org.datavault.common.event.Event;
import org.datavault.common.event.Error;

import org.apache.commons.io.FileUtils;

public class Withdraw extends Job {
    
    @Override
    public void performAction(Context context) {
        
        EventSender eventStream = (EventSender)context.getEventStream();
        
        System.out.println("\tWithdraw job - performAction()");
        
        Map<String, String> properties = getProperties();
        String depositId = properties.get("depositId");
        String bagID = properties.get("bagId");
        String withdrawalPath = properties.get("withdrawalPath");
        
        eventStream.send(new Event(depositId, "Withdrawal started"));
        
        System.out.println("\tbagID: " + bagID);
        System.out.println("\twithdrawalPath: " + withdrawalPath);
        
        try {
            Path withdrawPath = Paths.get(withdrawalPath);
            File withdrawDir = withdrawPath.toFile();

            if (!withdrawDir.exists() || !withdrawDir.isDirectory()) {
                // Target path must exist and be a directory
                System.out.println("\tTarget directory not found!");
            }

            // Find the archived data
            // TODO: a filesystem driver should transform a bagID to an actual path
            String tarFileName = bagID + ".tar";
            Path archivePath = Paths.get(context.getArchiveDir()).resolve(tarFileName);
            File archiveFile = archivePath.toFile();

            // Copy the tar file to the withdrawal area
            System.out.println("\tCopying tar file from archive ...");
            File withdrawFile = withdrawPath.resolve(tarFileName).toFile();
            FileUtils.copyFile(archiveFile, withdrawFile);
            
            System.out.println("\tWithdrawal complete: " + withdrawFile);
            eventStream.send(new Event(depositId, "Withdrawal complete"));
            
        } catch (Exception e) {
            e.printStackTrace();
            eventStream.send(new Error(depositId, "Withdrawal failed: " + e.getMessage()));
        }
    }
}
