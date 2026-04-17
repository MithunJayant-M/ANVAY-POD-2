package com.cts.mfrp.anvay.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "institutions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Institution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "institution_id")
    private Long institutionId;

    @Column(name = "name")
    private String name;

    @Column(name="institution_email")
    private String institutionEmail;

    @Column(name="status")
    private String status;

    @Column(name = "location")
    private String location;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    private List<Club> club=new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
    private List<User> users=new ArrayList<>();
}
