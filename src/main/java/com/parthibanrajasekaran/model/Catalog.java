package com.parthibanrajasekaran.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="Inventory")
@JsonInclude(Include.NON_NULL)
public class Catalog {

  @Getter
  @Setter
  @Column(name = "course")
  private String course = null;

  @Getter
  @Setter
  @Id
  @Column(name = "id")
  private String id= null;

  @Getter
  @Setter
  @Column(name = "category")
  private int category = 0;

  @Getter
  @Setter
  @Column(name = "author")
  private String author= null;


  public Catalog(String course, String id, int category, String author) {
    this.course = course;
    this.id = id;
    this.category = category;
    this.author = author;
  }

  public Catalog() {
  }
}
