package org.nextlevel.language;

import org.nextlevel.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/languages")
public class LanguageController {
	@Autowired
	private LanguageService service;

	@Autowired
	private LanguageRepository languageRepository;

	@PostMapping()
	public ResponseEntity<Language> save (@RequestBody Language language) {
		List<User> users = language.getUsers();
		return ResponseEntity.ok(languageRepository.save(language));
	}

	@GetMapping()
	public ResponseEntity<List<Language>> get () {
		return ResponseEntity.ok(languageRepository.findAll());
	}

	@GetMapping("{id}")
	public ResponseEntity<Language> read(@PathVariable("id") Long id) {
		Language language = service.get(id);
		if (language == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(language);
		}
	}

	@PutMapping("{id}")
	public ResponseEntity<Language> update (@PathVariable Long id, @RequestBody Language language) {
		Language languageOld = service.get(id);
		if (languageOld != null) {
			if (language.getName() != null && language.getName().trim().length()>0) {
				languageOld.setName(language.getName());
			}

			List<User> users = language.getUsers();
			if(null != users) {
				languageOld.setUsers(users);
			}

			return ResponseEntity.ok(languageRepository.save(languageOld));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("{id}")
	public ResponseEntity<HttpStatus> delete (@PathVariable Long id) {
		languageRepository.deleteById(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

}
