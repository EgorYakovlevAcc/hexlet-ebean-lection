package model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "users")
public class User {
    @Id
    private Integer id;
    private String name;
    private Date birthdate;
    private String lastname;

    public User() {
    }

    public User(String name, Date birthdate, String lastname) {
        super();
        this.name = name;
        this.birthdate = birthdate;
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", birthdate=" + birthdate +
                ", lastname='" + lastname + '\'' +
                ", id=" + id +
                '}';
    }
}
