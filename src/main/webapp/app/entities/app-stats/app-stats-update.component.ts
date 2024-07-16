import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import AppStatsService from './app-stats.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import { type IAppStats, AppStats } from '@/shared/model/app-stats.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AppStatsUpdate',
  setup() {
    const appStatsService = inject('appStatsService', () => new AppStatsService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const appStats: Ref<IAppStats> = ref(new AppStats());
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveAppStats = async appStatsId => {
      try {
        const res = await appStatsService().find(appStatsId);
        appStats.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.appStatsId) {
      retrieveAppStats(route.params.appStatsId);
    }

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      usedTenantId: {
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 1 }).toString(), 1),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 254 }).toString(), 254),
      },
    };
    const v$ = useVuelidate(validationRules, appStats as any);
    v$.value.$validate();

    return {
      appStatsService,
      alertService,
      appStats,
      previousState,
      isSaving,
      currentLanguage,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.appStats.id) {
        this.appStatsService()
          .update(this.appStats)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('kaizenMobileApp.appStats.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.appStatsService()
          .create(this.appStats)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('kaizenMobileApp.appStats.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
