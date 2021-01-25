package ru.otus.core.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Table(name = "address_data_set")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode(exclude = "client")
@ToString(exclude = "client")
public class AddressDataSet {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ADDRESS_GENERATOR")
  @SequenceGenerator(name = "SEQ_ADDRESS_GENERATOR", sequenceName = "SEQ_ADDRESS", allocationSize = 1)
  private Long id;

  @Column(name = "phone_number", nullable = false)
  private String street;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "client_id")
  private Client client;
}
