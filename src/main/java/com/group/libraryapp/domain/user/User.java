package com.group.libraryapp.domain.user;

import com.group.libraryapp.domain.user.loanhistory.UserLoanHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

	@Id // 아이디라는 걸 알려주기 위함
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment를 위함
	private Long id = null;

	@Column(nullable = false, length = 20) // name varchar2(20)
	private String name;
	private int age;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserLoanHistory> userLoanHistories = new ArrayList<>();

	protected User() {}

	public User(String name, int age) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException(String.format("잘못된 name(%s)이 들어왔습니다", name));
		}

		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public Long getId() {
		return id;
	}

	public void updateName(String name) {
		this.name = name;
	}

	public void loanBook(String bookName) {
		this.userLoanHistories.add(new UserLoanHistory(this, bookName));
	}

	public void returnBook(String bookName) {
		UserLoanHistory targetHistory = this.userLoanHistories.stream()
				.filter(history -> history.getBookName().equals(bookName))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
		targetHistory.doReturn();;
	}
}
