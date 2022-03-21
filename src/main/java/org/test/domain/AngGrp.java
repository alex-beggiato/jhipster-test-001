package org.test.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A AngGrp.
 */
@Entity
@Table(name = "ang_grp")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AngGrp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Type(type = "uuid-char")
    @Column(name = "uid", length = 36)
    private UUID uid;

    @Column(name = "grp_cod")
    private String grpCod;

    @Column(name = "grp_dsc")
    private String grpDsc;

    @ManyToMany(mappedBy = "uids")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "bdgUid", "uids" }, allowSetters = true)
    private Set<AngRes> uids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AngGrp id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUid() {
        return this.uid;
    }

    public AngGrp uid(UUID uid) {
        this.setUid(uid);
        return this;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getGrpCod() {
        return this.grpCod;
    }

    public AngGrp grpCod(String grpCod) {
        this.setGrpCod(grpCod);
        return this;
    }

    public void setGrpCod(String grpCod) {
        this.grpCod = grpCod;
    }

    public String getGrpDsc() {
        return this.grpDsc;
    }

    public AngGrp grpDsc(String grpDsc) {
        this.setGrpDsc(grpDsc);
        return this;
    }

    public void setGrpDsc(String grpDsc) {
        this.grpDsc = grpDsc;
    }

    public Set<AngRes> getUids() {
        return this.uids;
    }

    public void setUids(Set<AngRes> angRes) {
        if (this.uids != null) {
            this.uids.forEach(i -> i.removeUid(this));
        }
        if (angRes != null) {
            angRes.forEach(i -> i.addUid(this));
        }
        this.uids = angRes;
    }

    public AngGrp uids(Set<AngRes> angRes) {
        this.setUids(angRes);
        return this;
    }

    public AngGrp addUid(AngRes angRes) {
        this.uids.add(angRes);
        angRes.getUids().add(this);
        return this;
    }

    public AngGrp removeUid(AngRes angRes) {
        this.uids.remove(angRes);
        angRes.getUids().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AngGrp)) {
            return false;
        }
        return id != null && id.equals(((AngGrp) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AngGrp{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", grpCod='" + getGrpCod() + "'" +
            ", grpDsc='" + getGrpDsc() + "'" +
            "}";
    }
}
