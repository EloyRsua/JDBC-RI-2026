package uo.ri.cws.application.service;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import io.cucumber.junit.platform.engine.Constants;

@Suite

// common basic tests 
@SelectClasspathResource("uo/ri/cws/application/service/acceptance/mechanic")
@SelectClasspathResource("uo/ri/cws/application/service/acceptance/invoice")
@SelectClasspathResource("uo/ri/cws/application/service/acceptance/workorder")

@ConfigurationParameter(
	key = Constants.PLUGIN_PROPERTY_NAME, 
	value = "pretty, html:target/cucumber-results.html"
)
@ConfigurationParameter(
	key = Constants.SNIPPET_TYPE_PROPERTY_NAME, 
	value = "camelcase"
)
@ConfigurationParameter(
	key = Constants.GLUE_PROPERTY_NAME, 
	value = "uo.ri.cws.application.service.acceptance"
)
public class RunServiceTests_Basic {}
