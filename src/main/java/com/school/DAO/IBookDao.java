package com.school.DAO;

import com.school.Entity.BookDTO;

import java.util.List;

public interface IBookDao {
	List<BookDTO> selectAllBooks();
}
