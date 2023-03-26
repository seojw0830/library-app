package com.group.libraryapp.dto.user.request;

public class UserCreateRequest {

	private final String name;
	private final int age;

	public UserCreateRequest(String name, int age) {
		this.name = name;
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}
}
