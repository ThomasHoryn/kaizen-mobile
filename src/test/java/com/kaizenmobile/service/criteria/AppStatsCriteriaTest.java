package com.kaizenmobile.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AppStatsCriteriaTest {

    @Test
    void newAppStatsCriteriaHasAllFiltersNullTest() {
        var appStatsCriteria = new AppStatsCriteria();
        assertThat(appStatsCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void appStatsCriteriaFluentMethodsCreatesFiltersTest() {
        var appStatsCriteria = new AppStatsCriteria();

        setAllFilters(appStatsCriteria);

        assertThat(appStatsCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void appStatsCriteriaCopyCreatesNullFilterTest() {
        var appStatsCriteria = new AppStatsCriteria();
        var copy = appStatsCriteria.copy();

        assertThat(appStatsCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(appStatsCriteria)
        );
    }

    @Test
    void appStatsCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var appStatsCriteria = new AppStatsCriteria();
        setAllFilters(appStatsCriteria);

        var copy = appStatsCriteria.copy();

        assertThat(appStatsCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(appStatsCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var appStatsCriteria = new AppStatsCriteria();

        assertThat(appStatsCriteria).hasToString("AppStatsCriteria{}");
    }

    private static void setAllFilters(AppStatsCriteria appStatsCriteria) {
        appStatsCriteria.id();
        appStatsCriteria.usedTenantId();
        appStatsCriteria.distinct();
    }

    private static Condition<AppStatsCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) && condition.apply(criteria.getUsedTenantId()) && condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AppStatsCriteria> copyFiltersAre(AppStatsCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUsedTenantId(), copy.getUsedTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
