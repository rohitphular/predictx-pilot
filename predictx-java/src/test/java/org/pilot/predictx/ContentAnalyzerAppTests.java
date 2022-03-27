package org.pilot.predictx;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
@Suite
@SelectPackages({"org.pilot.predictx.service"})
class ContentAnalyzerAppTests {

	@Test
	void contextLoads() {
	}

}