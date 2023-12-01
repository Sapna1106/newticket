package com.example.TicketModule;

import com.example.TicketModule.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
class TicketModuleApplicationTests {

	@Autowired
	TicketRepository ticketRepository;


	@Test
	void contextLoads() {
	}

//	@Test
//	public void testSaveAndRetrieveEntity() {
//		// Given
//		YourEntity entity = new YourEntity();
//		Map<String, Object> jsonData = new HashMap<>();
//		jsonData.put("note", "hello");
//		jsonData.put("key", "value");
//
//		entity.setJsonData(jsonData);
//
//		// When
//		yourEntityRepository.save(entity);
//	}

	@Test
	public void testFindByJsonDataKey() {
		String keyToSearch = "$.sortnote";
		Long id = 1L;
		Optional<String> data = ticketRepository.findNoteByIdAndNotePath(id,keyToSearch);
    System.out.println(data.get());
	}

}
