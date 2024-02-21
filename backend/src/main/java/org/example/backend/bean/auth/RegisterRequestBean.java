package org.example.backend.bean.auth;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestBean {
	private String email;
	private String firstName;
	private String lastName;
	private String password;
}
