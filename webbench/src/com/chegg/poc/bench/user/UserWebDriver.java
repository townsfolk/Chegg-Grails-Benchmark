/*
 * Copyright (c) 2009-2010 Shanti Subramanyam, Akara Sucharitakul

 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
package com.chegg.poc.bench.user;

import com.sun.faban.driver.*;
import com.sun.faban.driver.util.ContentSizeStats;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Basic web workload driver drives only one operation via a get request.
 */
@BenchmarkDefinition(
		name = "User Web Workload",
		version = "0.1",
		metric = "req/s"
)
@BenchmarkDriver(
		name = "UserWeb",
		threadPerScale = (float) 1,
		opsUnit = "requests",
		metric = "req/s",
		responseTimeUnit = TimeUnit.MILLISECONDS
)
@NegativeExponential(
		cycleType = CycleType.THINKTIME,
		cycleMin = 200,
		cycleMax = 1000,
		cycleMean = 500,
		cycleDeviation = 2
)
@FixedSequence({"Create", "Save", "Show"})
public class UserWebDriver {

	private static final String[] firstNames = new String[]{
			"Eric", "Stan", "Kyle", "Kenny", "Wendy", "Duke", "Goofy", "Mickey", "Donald", "Daisy", "Minny"
	};
	private static final String[] lastNames = new String[]{
			"Cartman", "Marsh", "Broflovski", "McCormick", "Testaburger", "Java", "Dawg", "Mouse", "Duck"
	};

	private DriverContext ctx;
	private HttpTransport http;
	private Logger logger;
	private String createUrl = "http://localhost:8092/user-grails-client/user/create";
	private String saveUrl = "http://localhost:8092/user-grails-client/user/save";
	private String showUrl = "http://localhost:8092/user-grails-client/user/show";
	private long userId;

	ContentSizeStats contentStats = null;

	/**
	 * Constructs the basic web workload driver.
	 *
	 * @throws XPathExpressionException An XPath error occurred
	 */
	public UserWebDriver() throws XPathExpressionException, ConfigurationException {
		ctx = DriverContext.getContext();
		HttpTransport.setProvider(
				"com.sun.faban.driver.transport.hc3.ApacheHC3Transport");
		http = HttpTransport.newInstance();

		String appHost = ctx.getProperty("appHost");
		String appContext = ctx.getProperty("appContext");


		createUrl = appHost + appContext + "/user/create";
		saveUrl = appHost + appContext + "/user/save";
		showUrl = appHost + appContext + "/user/show";

		contentStats = new ContentSizeStats(ctx.getOperationCount());
		ctx.attachMetrics(contentStats);
		logger = ctx.getLogger();
	}

	/**
	 * Retrieves the Create page. Should just be a html page with a form on it for firstname and lastname.
	 *
	 * @throws IOException An I/O or network error occurred.
	 */
	@BenchmarkOperation(
			name = "Create",
			max90th = 1000, // millisec
			timing = Timing.AUTO
	)
	public void doCreate() throws IOException {
		StringBuilder content = http.fetchURL(createUrl);
		if (ctx.isTxSteadyState()) {
			contentStats.sumContentSize[ctx.getOperationId()] += content.length();
		}
	}

	/**
	 * Posts firstname and lastname to the app to create a new user. Expects to get the ID back somehow.
	 *
	 * @throws IOException
	 */
	@BenchmarkOperation(
			name = "Save",
			max90th = 1000,
			timing = Timing.AUTO
	)
	public void doSave() throws IOException {
		int firstNameIndex = ctx.getRandom().random(0, 10);
		int lastNameIndex = ctx.getRandom().random(0, 8);
		String firstName = firstNames[firstNameIndex];
		String lastName = lastNames[lastNameIndex];
		//logger.info("Creating new user - first name: " + firstName + ", last name: " + lastName);
		StringBuilder content = http.fetchURL(saveUrl, "firstName=" + firstName + "&lastName=" + lastName);
		try {
			userId = Long.parseLong(content.toString().trim());
		} catch (NumberFormatException nfe) {
			logger.log(Level.SEVERE, "Content didn't contain an ID: " + content, nfe);
		}
		//logger.info("    User Created? content: " + content + ", ID: " + userId);
		if (ctx.isTxSteadyState()) {
			contentStats.sumContentSize[ctx.getOperationId()] += content.length();
		}
	}

	@BenchmarkOperation(
			name = "Show",
			max90th = 1000,
			timing = Timing.AUTO
	)
	public void doShow() throws IOException {
		StringBuilder content = http.fetchURL(showUrl + "/" + userId);
		if (ctx.isTxSteadyState()) {
			contentStats.sumContentSize[ctx.getOperationId()] += content.length();
		}
	}
}
