package org.nextlevel.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.username = :username")
	public User getUserByUsername(@Param("username") String username);

	@Query("SELECT u FROM User u WHERE u.phoneNo = :phoneNo")
	public User getUserByPhoneNo(@Param("phone") Long phoneNo);

	@Modifying
	@Query("UPDATE User u SET u.authType = ?2 WHERE u.username = ?1")
	public void updateAuthenticationType(String username, AuthenticationType authType);

	@Modifying
	@Query("UPDATE User u SET u.authType = ?2, u.profileImageURL = ?3 WHERE u.username = ?1")
	public void updateUserLogin(String username, AuthenticationType authType, String profileURL);
}
