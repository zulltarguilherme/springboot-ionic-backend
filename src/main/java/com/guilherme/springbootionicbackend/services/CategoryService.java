package com.guilherme.springbootionicbackend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.guilherme.springbootionicbackend.domain.Category;
import com.guilherme.springbootionicbackend.dto.CategoryDTO;
import com.guilherme.springbootionicbackend.repositories.CategoryRepository;
import com.guilherme.springbootionicbackend.services.exceptions.DataIntegrityException;
import com.guilherme.springbootionicbackend.services.exceptions.ObjectNotFountException;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repo;

	public Category find(Integer id) {
		Optional<Category> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFountException("Object not found! Id: " + id + ", Type: " + Category.class.getName()));
	}

	public Category insert(Category obj) {
		obj.setId(null);
		return repo.save(obj);
	}

	public Category update(Category obj) {
		Category newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}

	private void updateData(Category newObj, Category obj) {
		newObj.setName(obj.getName());
	}

	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("It is not possible to exclude a category containing products.");
		}
	}

	public List<Category> findAll() {
		return repo.findAll();
	}

	public Page<Category> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}

	public Category fromDto(CategoryDTO objDto) {
		return new Category(objDto.getId(), objDto.getName());
	}
}
