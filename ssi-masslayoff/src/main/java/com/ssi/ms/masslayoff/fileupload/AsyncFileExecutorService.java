package com.ssi.ms.masslayoff.fileupload;

import java.io.File;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * @author Praveenraja Paramsivam
 * Configuration class for setting up an asynchronous file executor service.
 * This class provides configuration settings for managing asynchronous file-related tasks.
 */
@Configuration
public class AsyncFileExecutorService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AsyncFileExecutorService.class);
	@Autowired
	private ExecutorService executorService;
	@Autowired
	private ApplicationContext applicationContext;
	/**
	 * Reads a file and performs specific actions based on the provided parameters.
	 *
	 * @param newFile {@link File} The file to be read.
	 * @param userId {@link String} The user ID associated with the file reading operation.
	 * @param musmId {@link Long} The ID related to the operation context.
	 * @return {@link boolean} A boolean indicating the success or failure of the file reading operation.
	 */
	public boolean fileRead(File newFile, String userId, Long musmId) {
		final FileProcessorTask fileProcessor = applicationContext.getBean(FileProcessorTask.class, newFile, userId, musmId);
		executorService.execute(fileProcessor);
		return true;
	}

	public void fileProcessor() {
		/*
		 * for (int i = 0 ; i < 5 ; i ++) { LOGGER.error("In side the lool-->"+ i);
		 * FileProcessorTaskTester fileProcessor =
		 * applicationContext.getBean(FileProcessorTaskTester.class,
		 * mslUploadErrorMuseRepository, "FileName", 6L, i);
		 * executorService.execute(fileProcessor); }
		 */
		LOGGER.error("Task submittion completed");
	}
}
