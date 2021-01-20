package ru.otus.core.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NamedQuery;

@NamedQuery(
    name = "client.findAll",
    query = "select c from Client c"
)
@Data
@NoArgsConstructor
@Entity
@Table(name = "client")
@EqualsAndHashCode(of = "id")
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CLIENT_GENERATOR")
  @SequenceGenerator(name = "SEQ_CLIENT_GENERATOR", sequenceName = "SEQ_CLIENT", allocationSize = 1)
  private long id;
  private String name;
  private int age;
  @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
  private AddressDataSet address;
  @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<PhoneDataSet> phones = new ArrayList<>();

  public Client(long id, String name, int age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }
}
