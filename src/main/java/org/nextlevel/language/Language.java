package org.nextlevel.language;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.nextlevel.user.User;
import org.checkerframework.common.aliasing.qual.Unique;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "languages")
public class Language {
	@Id
	@Column(name="language_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long languageId;

	@NotBlank(message = "Language Name is Mandatory!")
	@Unique
	private String name;

	@ManyToMany(mappedBy = "languages", targetEntity = User.class, fetch = FetchType.EAGER)
	@JsonIgnore
	private List<User> users;

	public Language() {}

	public Language(Long languageId) {
		this.languageId=languageId;
	}

	public Long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
