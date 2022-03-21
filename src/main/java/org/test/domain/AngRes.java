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
import org.test.domain.enumeration.AngResTyp;

/**
 * A AngRes.
 */
@Entity
@Table(name = "ang_res")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AngRes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Type(type = "uuid-char")
    @Column(name = "uid", length = 36)
    private UUID uid;

    @Column(name = "res_cod")
    private String resCod;

    @Column(name = "res_dsc")
    private String resDsc;

    @Column(name = "bdg_uid")
    private Long bdgUid;

    @Enumerated(EnumType.STRING)
    @Column(name = "res_typ")
    private AngResTyp resTyp;

    @OneToOne
    @JoinColumn(unique = true)
    private AngBdg bdgUid;

    @ManyToMany
    @JoinTable(name = "rel_ang_res__uid", joinColumns = @JoinColumn(name = "ang_res_id"), inverseJoinColumns = @JoinColumn(name = "uid_id"))
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "uids" }, allowSetters = true)
    private Set<AngGrp> uids = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AngRes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUid() {
        return this.uid;
    }

    public AngRes uid(UUID uid) {
        this.setUid(uid);
        return this;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getResCod() {
        return this.resCod;
    }

    public AngRes resCod(String resCod) {
        this.setResCod(resCod);
        return this;
    }

    public void setResCod(String resCod) {
        this.resCod = resCod;
    }

    public String getResDsc() {
        return this.resDsc;
    }

    public AngRes resDsc(String resDsc) {
        this.setResDsc(resDsc);
        return this;
    }

    public void setResDsc(String resDsc) {
        this.resDsc = resDsc;
    }

    public Long getBdgUid() {
        return this.bdgUid;
    }

    public AngRes bdgUid(Long bdgUid) {
        this.setBdgUid(bdgUid);
        return this;
    }

    public void setBdgUid(Long bdgUid) {
        this.bdgUid = bdgUid;
    }

    public AngResTyp getResTyp() {
        return this.resTyp;
    }

    public AngRes resTyp(AngResTyp resTyp) {
        this.setResTyp(resTyp);
        return this;
    }

    public void setResTyp(AngResTyp resTyp) {
        this.resTyp = resTyp;
    }

    public AngBdg getBdgUid() {
        return this.bdgUid;
    }

    public void setBdgUid(AngBdg angBdg) {
        this.bdgUid = angBdg;
    }

    public AngRes bdgUid(AngBdg angBdg) {
        this.setBdgUid(angBdg);
        return this;
    }

    public Set<AngGrp> getUids() {
        return this.uids;
    }

    public void setUids(Set<AngGrp> angGrps) {
        this.uids = angGrps;
    }

    public AngRes uids(Set<AngGrp> angGrps) {
        this.setUids(angGrps);
        return this;
    }

    public AngRes addUid(AngGrp angGrp) {
        this.uids.add(angGrp);
        angGrp.getUids().add(this);
        return this;
    }

    public AngRes removeUid(AngGrp angGrp) {
        this.uids.remove(angGrp);
        angGrp.getUids().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AngRes)) {
            return false;
        }
        return id != null && id.equals(((AngRes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AngRes{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", resCod='" + getResCod() + "'" +
            ", resDsc='" + getResDsc() + "'" +
            ", bdgUid=" + getBdgUid() +
            ", resTyp='" + getResTyp() + "'" +
            "}";
    }
}
