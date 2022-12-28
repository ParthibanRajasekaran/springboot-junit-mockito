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
@Table(name="Storage2")
@JsonInclude(Include.NON_NULL)
public class Library {

  @Getter
  @Setter
  @Column(name = "book_name")
  private String book_name = null;

  @Getter
  @Setter
  @Id
  @Column(name = "id")
  private String id= null;

  @Getter
  @Setter
  @Column(name = "aisle")
  private int aisle= 0;

  @Getter
  @Setter
  @Column(name = "isbn")
  private String isbn= null;

  @Getter
  @Setter
  @Column(name = "author")
  private String author= null;





}
