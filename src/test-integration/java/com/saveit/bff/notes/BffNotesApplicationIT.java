package com.saveit.bff.notes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("it")
@Import(TestcontainersConfiguration.class)
@SpringBootTest(classes = BffNotesApplicationIT.class)
class BffNotesApplicationIT {

	@Test
	void contextLoads() {
	}

}
