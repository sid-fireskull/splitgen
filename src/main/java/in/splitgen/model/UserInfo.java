package in.splitgen.model;

import lombok.Data;
import lombok.Getter;

@Data
public class UserInfo {
    private long id;
    private String email;
    private String name;
    private String sex;
    private String mobileNo;
    private String upiId;
    private String qrUuid;
    private String dob;

}
