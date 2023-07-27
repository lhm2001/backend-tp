package com.tesis.proyecto_tesis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;
import java.util.Date;
@Entity
@Table(name ="consultations")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConsultation;

    @NotNull
    @Column(length = 999999)
    private String photo;

    @NotNull
    private String createdDate;

    @NotNull
    private Boolean isDysplastic;

    @NotNull
    private String resultAssymetry;

    @NotNull
    private String resultBorder;

    @NotNull
    private String resultColor;

    @NotNull
    private String resultDiameter;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="category_id",nullable = false)
    @JsonIgnore
    private Category category;

    public Long getIdConsultation() {
        return idConsultation;
    }

    public Consultation setIdConsultation(Long idConsultation) {
        this.idConsultation = idConsultation;
        return this;
    }

    public String getPhoto() {
        return photo;
    }

    public Consultation setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public Consultation setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Boolean getDysplastic() {
        return isDysplastic;
    }

    public Consultation setDysplastic(Boolean dysplastic) {
        isDysplastic = dysplastic;
        return this;
    }

    public String getResultAssymetry() {
        return resultAssymetry;
    }

    public Consultation setResultAssymetry(String resultAssymetry) {
        this.resultAssymetry = resultAssymetry;
        return this;
    }

    public String getResultBorder() {
        return resultBorder;
    }

    public Consultation setResultBorder(String resultBorder) {
        this.resultBorder = resultBorder;
        return this;
    }

    public String getResultColor() {
        return resultColor;
    }

    public Consultation setResultColor(String resultColor) {
        this.resultColor = resultColor;
        return this;
    }

    public String getResultDiameter() {
        return resultDiameter;
    }

    public Consultation setResultDiameter(String resultDiameter) {
        this.resultDiameter = resultDiameter;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Consultation setCategory(Category category) {
        this.category = category;
        return this;
    }
}
