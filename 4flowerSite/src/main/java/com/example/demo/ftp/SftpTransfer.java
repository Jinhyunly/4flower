package com.example.demo.ftp;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.multipart.MultipartFile;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpTransfer {
    private static final Logger logger = LogManager.getLogger(SftpTransfer.class);

    public void transferLocalFileToRemoteHost(MultipartFile multipartFile, String fileName)  {
    //public void transferLocalFileToRemoteHost(String filenameWithPath) {
        JSch jsch = new JSch();
        Session session = null;
        try {
            /*
            * getSession() method required 3 parameters.
            * username of the remote server
            * IP address of the remote server
            * SSH port of the remote server
            */

        		//danger!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            session = jsch.getSession("sftp login user", "ip", 22); //sftp login user, ip, port
            logger.info("session created.");

            // war배포
            //String privateKey = "/home/ubuntu/.ssh/temp_key.pem";
            String privateKey = "";

          	try {
							privateKey = new ClassPathResource("/static/key").getFile().getAbsolutePath();
							logger.info("privateKey: "+privateKey);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
          	privateKey += "/temp_key.pem"; //key 파일명


          	logger.info("test1");

            //local
            //String privateKey =  "C:/dev/aws/ubuntu_webServer/test_key.pem";
            jsch.addIdentity(privateKey);
            logger.info("identity added");
            logger.info("test2");

            session.setConfig("StrictHostKeyChecking", "no");
            logger.info("Trying to connect...");
            session.connect();
            logger.info("Connected successfully.");


            Channel channel = session.openChannel("sftp");
            channel.connect();
            ChannelSftp sftpChannel = (ChannelSftp) channel;
            logger.info("Doing SFTP...");
            logger.info("test3");

            InputStream in = null;
            //FileOutputStream out = null;

            try {
            	in = multipartFile.getInputStream();

            	sftpChannel.cd("/var/lib/tomcat9/webapps/ROOT/WEB-INF/classes/static/gallery/");
            	sftpChannel.put(in, fileName);
              //in = sftpChannel.get(fileName);
            	logger.info("test4");
            	in.close();
            } catch (SftpException e) {
            	logger.info("test5:"+e);
            	e.printStackTrace();
            } catch (IOException e) {
            	logger.info("test6");
            	e.printStackTrace();
            }

            /*
            * put method takes two arguments.
            * first one is the file which located in our local machine
            * second one is the place our file should be transfer.
            */
            //sftpChannel.put(filenameWithPath, "/home/ec2-user");
            //filenameWithPath = "/var/lib/tomcat9/webapps";

            //String tempPath  = "/var/lib/tomcat9/webapps/ROOT/WEB-INF/classes/static/gallery/"+filenameWithPath;

            //sftpChannel.put(filenameWithPath);

            //sftpChannel.put(filenameWithPath, "/var/lib/tomcat9/webapps/ROOT/WEB-INF/classes/static/gallery");


            logger.info("Success");
            sftpChannel.exit();
            session.disconnect();
            logger.info("test7");

        //} catch (SftpException | JSchException e) {
        } catch (JSchException e) {
            logger.trace("Error occured during SFTP " + e.getMessage());
            logger.info("test8");
        }
    }
}