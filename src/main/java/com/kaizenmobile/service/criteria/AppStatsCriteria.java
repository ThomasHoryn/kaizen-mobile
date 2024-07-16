package com.kaizenmobile.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.kaizenmobile.domain.AppStats} entity. This class is used
 * in {@link com.kaizenmobile.web.rest.AppStatsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /app-stats?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppStatsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter usedTenantId;

    private Boolean distinct;

    public AppStatsCriteria() {}

    public AppStatsCriteria(AppStatsCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.usedTenantId = other.optionalUsedTenantId().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public AppStatsCriteria copy() {
        return new AppStatsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getUsedTenantId() {
        return usedTenantId;
    }

    public Optional<StringFilter> optionalUsedTenantId() {
        return Optional.ofNullable(usedTenantId);
    }

    public StringFilter usedTenantId() {
        if (usedTenantId == null) {
            setUsedTenantId(new StringFilter());
        }
        return usedTenantId;
    }

    public void setUsedTenantId(StringFilter usedTenantId) {
        this.usedTenantId = usedTenantId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AppStatsCriteria that = (AppStatsCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(usedTenantId, that.usedTenantId) && Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, usedTenantId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppStatsCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalUsedTenantId().map(f -> "usedTenantId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
