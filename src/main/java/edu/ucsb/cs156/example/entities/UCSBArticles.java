package edu.ucsb.cs156.example.entities;

import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "ucsbarticles")
public class UCSBArticles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    String title;
    String url;
    String explanation;
    String email;
    LocalDateTime dateAdded;
}