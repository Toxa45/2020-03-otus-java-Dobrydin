package ru.otus.core.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "phone_data_set")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "client")
@ToString(exclude = "client")
public class PhoneDataSet {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PHONE_GENERATOR")
  @SequenceGenerator(name = "SEQ_PHONE_GENERATOR", sequenceName = "SEQ_PHONE", allocationSize = 1)
  private Long id;

  @Column(name = "phone_number", nullable = false)
  private String number;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "client_id", nullable = false)
  private Client client;
}
