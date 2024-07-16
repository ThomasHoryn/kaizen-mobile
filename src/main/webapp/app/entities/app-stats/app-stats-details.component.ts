import { defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';

import AppStatsService from './app-stats.service';
import { type IAppStats } from '@/shared/model/app-stats.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AppStatsDetails',
  setup() {
    const appStatsService = inject('appStatsService', () => new AppStatsService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const appStats: Ref<IAppStats> = ref({});

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

    return {
      alertService,
      appStats,

      previousState,
      t$: useI18n().t,
    };
  },
});
