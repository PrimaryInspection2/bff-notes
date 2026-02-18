package com.saveit.bff.notes;

import org.springframework.boot.SpringApplication;

public class TestBffNotesApplication {

	public static void main(String[] args) {
		SpringApplication.from(BffNotesApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
