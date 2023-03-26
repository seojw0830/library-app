package com.group.libraryapp.service.book;

import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.domain.book.BookRepository;
import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.UserRepository;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;
import com.group.libraryapp.domain.user.loanhistory.UserLoanHistoryRepository;
import com.group.libraryapp.dto.book.request.BookCreateRequest;
import com.group.libraryapp.dto.book.request.BookLoanRequest;
import com.group.libraryapp.dto.book.request.BookReturnRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

	private final BookRepository bookRepository;
	private final UserLoanHistoryRepository loanHistoryRepository;
	private final UserRepository userRepository;

	public BookService(BookRepository bookRepository, UserLoanHistoryRepository loanHistoryRepository, UserRepository userRepository) {
		this.bookRepository = bookRepository;
		this.loanHistoryRepository = loanHistoryRepository;
		this.userRepository = userRepository;
	}

	@Transactional
	public void saveBook(BookCreateRequest request) {
		bookRepository.save(new Book(request.getName()));
	}

	@Transactional
	public void loanBook(BookLoanRequest request) {
		// 1. 책 정보를 가져온다.
		Book book = bookRepository.findByName(request.getBookName())
				.orElseThrow(IllegalArgumentException::new);

		// 2. 대출기록 정보를 확인하여 대출중인지 확인
		if (loanHistoryRepository.existsByBookNameAndIsReturn(book.getName(), false)) {
			// 3. 만약 대출중이라면 예외발생
			throw new IllegalArgumentException("대출되어 있는 책입니다");
		}

		// 4.유저 정보를 가져온다.
		User user = userRepository.findByName(request.getUserName())
				.orElseThrow(IllegalArgumentException::new);
		user.loanBook(book.getName());
	}

	@Transactional
	public void returnBook(BookReturnRequest request) {
		User user = userRepository.findByName(request.getUserName())
				.orElseThrow(IllegalAccessError::new);

		user.returnBook(request.getBookName());

	}
}
