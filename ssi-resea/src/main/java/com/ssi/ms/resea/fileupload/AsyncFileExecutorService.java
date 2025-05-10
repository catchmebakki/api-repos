package com.ssi.ms.resea.fileupload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.concurrent.ExecutorService;

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
	 * @param rusmId {@link Long} The ID related to the operation context.
	 * @return {@link boolean} A boolean indicating the success or failure of the file reading operation.
	 */
	@Transactional
	public boolean fileRead(String originalFileName, File newFile, String userId, Long rucsId, Long rusmId) {
		final FileProcessorTask fileProcessor = applicationContext.getBean(FileProcessorTask.class,
				originalFileName, newFile, userId, rucsId, rusmId);
		executorService.execute(fileProcessor);
		return true;
	}
}