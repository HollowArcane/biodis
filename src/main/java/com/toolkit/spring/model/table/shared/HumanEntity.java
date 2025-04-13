package com.toolkit.spring.model.table.shared;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@MappedSuperclass
public abstract class HumanEntity<T>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    @NotBlank(message = "le nom ne doit pas être vide")
    private String lastName;

    @Column(name = "phone_number")
    @Pattern(regexp = "^[+-]?[0-9]{10}$", message = "le numero de téléphone doit être valide")
    private String phoneNumber;

    @Column(name = "email")
    @Email
    private String email;
    
    public Integer getId()
    { return id; }

    public void setId(Integer id)
    { this.id = id; }

    public String getFirstName()
    { return firstName; }

    public String getLastName()
    { return lastName; }

    public String getPhoneNumber()
    { return phoneNumber; }

    public String getEmail()
    { return email; }

    public void setFirstName(String firstName)
    { this.firstName = nullIfBlank(firstName); }
    
    public void setLastName(String lastName)
    { this.lastName = nullIfBlank(lastName); }
    
    public void setPhoneNumber(String phoneNumber)
    { this.phoneNumber = nullIfBlank(phoneNumber); }
    
    public void setEmail(String email)
    { this.email = nullIfBlank(email); }
    
    // Utility method
    private String nullIfBlank(String s)
    { return s == null || s.trim().isEmpty() ? null: s; }

    @AssertTrue(message = "un numéro de téléphone ou un email doit être fourni.")
    public boolean isPhoneNumber()
    { return phoneNumber != null || email != null; }
}
