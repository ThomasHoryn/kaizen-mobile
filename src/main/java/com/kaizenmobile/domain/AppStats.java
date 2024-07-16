package com.kaizenmobile.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AppStats.
 */
@Entity
@Table(name = "app_stats")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppStats implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(min = 1, max = 254)
    @Column(name = "used_tenant_id", length = 254)
    private String usedTenantId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AppStats id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsedTenantId() {
        return this.usedTenantId;
    }

    public AppStats usedTenantId(String usedTenantId) {
        this.setUsedTenantId(usedTenantId);
        return this;
    }

    public void setUsedTenantId(String usedTenantId) {
        this.usedTenantId = usedTenantId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppStats)) {
            return false;
        }
        return getId() != null && getId().equals(((AppStats) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppStats{" +
            "id=" + getId() +
            ", usedTenantId='" + getUsedTenantId() + "'" +
            "}";
    }
}
