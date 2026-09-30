package com.smartattend.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public class AuthDto {

    public static class LoginRequest {
        @NotBlank(message = "Username or email is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;

        public LoginRequest() {
        }

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class JwtResponse {
        private String token;
        @SuppressWarnings("unused")
        private String accessToken;
        private String type = "Bearer";
        private String refreshToken;
        private Long id;
        private String username;
        private String fullName;
        private String email;
        private List<String> roles;
        private Long studentId;
        private Long facultyId;
        private String rollNo;
        private String registrationNo;
        private String departmentName;
        private String sectionName;

        public JwtResponse() {
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
            this.accessToken = token;
        }

        public String getAccessToken() {
            return token;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
            this.token = accessToken;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public Long getStudentId() {
            return studentId;
        }

        public void setStudentId(Long studentId) {
            this.studentId = studentId;
        }

        public Long getFacultyId() {
            return facultyId;
        }

        public void setFacultyId(Long facultyId) {
            this.facultyId = facultyId;
        }

        public String getRollNo() {
            return rollNo;
        }

        public void setRollNo(String rollNo) {
            this.rollNo = rollNo;
        }

        public String getRegistrationNo() {
            return registrationNo;
        }

        public void setRegistrationNo(String registrationNo) {
            this.registrationNo = registrationNo;
        }

        public String getDepartmentName() {
            return departmentName;
        }

        public void setDepartmentName(String departmentName) {
            this.departmentName = departmentName;
        }

        public String getSectionName() {
            return sectionName;
        }

        public void setSectionName(String sectionName) {
            this.sectionName = sectionName;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private final JwtResponse instance = new JwtResponse();

            public Builder token(String token) {
                instance.setToken(token);
                return this;
            }

            public Builder type(String type) {
                instance.setType(type);
                return this;
            }

            public Builder refreshToken(String refreshToken) {
                instance.setRefreshToken(refreshToken);
                return this;
            }

            public Builder id(Long id) {
                instance.setId(id);
                return this;
            }

            public Builder username(String username) {
                instance.setUsername(username);
                return this;
            }

            public Builder fullName(String fullName) {
                instance.setFullName(fullName);
                return this;
            }

            public Builder email(String email) {
                instance.setEmail(email);
                return this;
            }

            public Builder roles(List<String> roles) {
                instance.setRoles(roles);
                return this;
            }

            public Builder studentId(Long studentId) {
                instance.setStudentId(studentId);
                return this;
            }

            public Builder facultyId(Long facultyId) {
                instance.setFacultyId(facultyId);
                return this;
            }

            public Builder rollNo(String rollNo) {
                instance.setRollNo(rollNo);
                return this;
            }

            public Builder registrationNo(String registrationNo) {
                instance.setRegistrationNo(registrationNo);
                return this;
            }

            public Builder departmentName(String departmentName) {
                instance.setDepartmentName(departmentName);
                return this;
            }

            public Builder sectionName(String sectionName) {
                instance.setSectionName(sectionName);
                return this;
            }

            public JwtResponse build() {
                return instance;
            }
        }
    }

    public static class TokenRefreshRequest {
        @NotBlank(message = "Refresh token cannot be blank")
        private String refreshToken;

        public TokenRefreshRequest() {
        }

        public TokenRefreshRequest(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }

    public static class TokenRefreshResponse {
        private String accessToken;
        private String refreshToken;
        private String tokenType = "Bearer";

        public TokenRefreshResponse() {
        }

        public TokenRefreshResponse(String accessToken, String refreshToken, String tokenType) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
            this.tokenType = tokenType != null ? tokenType : "Bearer";
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {
            private final TokenRefreshResponse instance = new TokenRefreshResponse();

            public Builder accessToken(String accessToken) {
                instance.setAccessToken(accessToken);
                return this;
            }

            public Builder refreshToken(String refreshToken) {
                instance.setRefreshToken(refreshToken);
                return this;
            }

            public Builder tokenType(String tokenType) {
                instance.setTokenType(tokenType);
                return this;
            }

            public TokenRefreshResponse build() {
                return instance;
            }
        }
    }

    public static class UserDto {
        private Long id;
        private String username;
        private String fullName;
        private String email;
        private String phone;
        private Boolean isActive;
        private List<String> roles;

        public UserDto() {
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }
}
