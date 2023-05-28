package org.nextlevel.language;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class LanguageService {
	@Autowired
	private LanguageRepository repo;
	
	public List<Language> listAll() {
		return repo.findAll();
	}

	public List<Language> listAll(Long orgId, Long languageId) {
		return repo.findAll()
				.stream()
				.filter(language -> language.getLanguageId().longValue() == languageId.longValue())
				.collect(Collectors.toList());
	}

	public void save(Language language) {
		repo.save(language);
	}
	
	public Language get(Long id) {
		return repo.findById(id).get();
	}
	
	public void delete(Long id) { repo.deleteById(id); }

	public Language getByName(String name) {
		Language languageSelected = null;
		List<Language> languageList = new ArrayList<>();
		Supplier<Stream<Language>> languagesStreamSupp = () -> repo.findAll()
				.stream()
				.filter(subject -> subject.getName().equalsIgnoreCase(name));

		languageList = languagesStreamSupp.get().collect(Collectors.toList());

		if (null != languageList && languageList.size() == 1) {
			languageSelected = languageList.get(0);
		}

		return languageSelected;
	}
}
