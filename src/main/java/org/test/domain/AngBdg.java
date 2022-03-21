package org.test.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A AngBdg.
 */
@Entity
@Table(name = "ang_bdg")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AngBdg implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Type(type = "uuid-char")
    @Column(name = "uid", length = 36)
    private UUID uid;

    @NotNull
    @Column(name = "bdg_cod", nullable = false)
    private String bdgCod;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AngBdg id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUid() {
        return this.uid;
    }

    public AngBdg uid(UUID uid) {
        this.setUid(uid);
        return this;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getBdgCod() {
        return this.bdgCod;
    }

    public AngBdg bdgCod(String bdgCod) {
        this.setBdgCod(bdgCod);
        return this;
    }

    public void setBdgCod(String bdgCod) {
        this.bdgCod = bdgCod;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AngBdg)) {
            return false;
        }
        return id != null && id.equals(((AngBdg) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AngBdg{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", bdgCod='" + getBdgCod() + "'" +
            "}";
    }
}
