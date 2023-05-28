package org.nextlevel.address;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {
	@Autowired
	private AddressRepository repo;
	
	public List<Address> listAll() {
		return repo.findAll();
	}
	
	public void save(Address address) {
		repo.save(address);
	}
	
	public Address get(Long id) {
		return repo.findById(id).get();
	}
	
	public void delete(Long id) { repo.deleteById(id); }
}
