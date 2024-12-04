package edu.alexu.cse.dripmeup.Service.Builder;

import edu.alexu.cse.dripmeup.Entity.EntityIF;
import edu.alexu.cse.dripmeup.Entity.Person;
import edu.alexu.cse.dripmeup.Enumeration.Gender;
import edu.alexu.cse.dripmeup.Enumeration.Role;

public abstract class Builder {
    private final Person person;

    public Builder(){
        this.person = new Person();
    }

    public abstract void build();

    public void buildRole(Role role){
        this.person.setRole(role);
    }

    public void buildUserName(String userName){
        this.person.setUsername(userName);
    }

    public void buildPersonID(String PersonID){
        this.person.setPersonID(person.getPersonID());
    }

    public void buildName(String name){
        this.person.setName(name);
    }

    public void buildPhoto(String photo){
        this.person.setPhoto(photo);
    }

    public void buildEmail(String email){
        this.person.setEmail(email);
    }

    public void buildGender(Gender gender){
        this.person.setGender(gender);
    }

    public Person getResult() {
        return this.person;
    }

    public EntityIF toEntity(){
        return person.toEntity();
    }

}
