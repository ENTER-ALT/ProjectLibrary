package be.ucll.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @NotBlank(message = INVALID_BIO_EXCEPTION)
    private String bio;

    @NotBlank(message = INVALID_LOCATION_EXCEPTION)
    private String location;

    @NotBlank(message = INVALID_INTERESTS_EXCEPTION)
    private String interests;

    public static final String INVALID_BIO_EXCEPTION = "Bio is required";
    public static final String INVALID_LOCATION_EXCEPTION = "Location is required";
    public static final String INVALID_INTERESTS_EXCEPTION = "Interests are required";

    public Profile() {
    }

    public Profile(String bio, String location, String interests) {
        setBio(bio);
        setLocation(location);
        setInterests(interests);
    }

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInterests() {
        return interests;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public Profile copyProfile(Profile other) {
        this.setLocation(other.getLocation());
        this.setInterests(other.getInterests());
        this.setBio(other.getBio());

        return this;
    }
}
