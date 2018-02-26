package com.security.zap.utils.boot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.security.zap.utils.ZapInfo;
import com.security.zap.utils.exception.ZapInitializationException;

/**
 * Class responsible to start and stop ZAP locally.
 * <p>
 * <b>ZAP must be installed locally for this to work.</b>
 * <p>
 * This will normally be used when ZAP's {@code zapInfo.path} was provided and {@code shouldRunWithDocker} is false.
 * 

 */
public class ZapLocalBoot extends AbstractZapBoot {

	private static final Logger LOGGER = LoggerFactory.getLogger(ZapLocalBoot.class);
	
	private static Process zap;
	
	@Override
	public void startZap(ZapInfo zapInfo) {
		int port = zapInfo.getPort();
		
		if (isZapRunning(port)) {
			LOGGER.info("ZAP is already up and running! No attempts will be made to start ZAP.");
			return;
		}
		
		try {
			start(zapInfo);
			waitForZapInitialization(port, zapInfo.getInitializationTimeoutInMillis());
		} catch (IOException e) {
			LOGGER.error("Error starting ZAP.", e);
		}
	}
	
	@Override
	public void stopZap() {
		if (zap != null) {
			LOGGER.info("Stopping ZAP.");
			zap.destroy();
		}
	}

	private static void start(ZapInfo zapInfo) throws IOException {
		String startCommand = buildStartCommand(zapInfo);
		ProcessBuilder processBuilder = new ProcessBuilder(startCommand.split(" +"));
		processBuilder.directory(getZapWorkingDirectory(zapInfo));
		
		Files.createDirectories(Paths.get(DEFAULT_ZAP_LOG_PATH));
		processBuilder.redirectOutput(new File(DEFAULT_ZAP_LOG_PATH, DEFAULT_ZAP_LOG_FILE_NAME));
		
		LOGGER.info("Starting ZAP with command: {}", startCommand);
		zap = processBuilder.start();
	}
	
	private static String buildStartCommand(ZapInfo zapInfo) {
		StringBuilder startCommand = new StringBuilder();
		
		startCommand.append("java").append(" ");
		startCommand.append(zapInfo.getJmvOptions()).append(" ");
		startCommand.append("-jar").append(" ");
		
		try {
			String zapJarName = retrieveZapJarName(zapInfo.getPath());
			startCommand.append(zapJarName).append(" ");
		} catch (IOException e) {
			LOGGER.error("Error retrieving ZAP's JAR file.");
		}
		
		String options = zapInfo.getOptions();
		startCommand.append(options != null ? options : DEFAULT_ZAP_OPTIONS);
		startCommand.append(" -port ").append(zapInfo.getPort());
		
		return startCommand.toString();
	}

	private static String retrieveZapJarName(String path) throws IOException {
		Path zapPath = Paths.get(path);
		if (isJarFile(zapPath)) {
			String filename = zapPath.getFileName().toString();
			LOGGER.debug("ZapPath points to the Jar file {}", filename);
			return filename;
		}
		
		LOGGER.debug("ZapPath points to the folder {}", zapPath.getFileName().toString());
		
		for (Path p : Files.newDirectoryStream(zapPath)) {
			if (isJarFile(p)) {
				String filename = p.getFileName().toString();
				LOGGER.debug("Chosen Zap Jar file {}", filename);
				return filename;
			}
		}
		
		throw new ZapInitializationException("ZAP's JAR file was not found.");
	}
	
	private static boolean isJarFile(Path path) {
		if (path == null) {
			return false;
		}
		
		if (!Files.isRegularFile(path)) {
			return false;
		}
		
		Path fileName = path.getFileName();
		
		if (fileName == null) {
			return false;
		}
		
		if (fileName.toString().endsWith(".jar")) {
			return true;
		}

		return false;
	}
	
	private static File getZapWorkingDirectory(ZapInfo zapInfo) {
		String fullPath = zapInfo.getPath();
		File dir = new File(fullPath);
		if (dir.isDirectory()) {
			return dir;
		}
		
		if (dir.isFile()) {
			return dir.getParentFile();
		}
		
		return new File(System.getProperty("user.dir"));
	}
	
}
