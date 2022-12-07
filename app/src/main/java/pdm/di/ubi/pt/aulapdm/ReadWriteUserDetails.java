package pdm.di.ubi.pt.aulapdm;

public class ReadWriteUserDetails {
    public String fullname,password,email,phone;


    public ReadWriteUserDetails(String email,String fullname,String phone,String password) {
        this.fullname=fullname;
        this.email=email;
        this.phone=phone;
        this.password=password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    @Override
    public String toString() {
        return "ReadWriteUserDetails{" +
                "fullname='" + fullname + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
