package model;

import java.util.List;

public class Contact {
    private Long id;
    private String name;
    private List<String> phoneNumbers;

    public Contact(String name, List<String> phoneNumbers) {
        setName(name);
        setPhoneNumbers(phoneNumbers);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    @Override
    public String toString() {
        return name + ": " + phoneNumbers;
    }
}
