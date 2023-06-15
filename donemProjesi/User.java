package donemProjesi;


public class User{
	private String username;
	private String password;
	private UserType userType;
	private static boolean parentCreated = false; 
	//enumu biraz kullanmış olmak için kullandım galiba. ama her kullanıcınıun adı, şifresi ve tipi var. ona göre yaratılıyorlar ve sadece bir ebeveyn var
	public User(String username, String password, UserType userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        if (userType == UserType.PARENT) {
            if (parentCreated) {
                throw new IllegalStateException("Sadece bir tane ebeveyn olabilir.");
            } else {
                parentCreated = true;
            }
        }
    }
	
	public UserType getUserType() {
		return userType;
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

