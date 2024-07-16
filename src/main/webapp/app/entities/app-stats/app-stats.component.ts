import { defineComponent, inject, onMounted, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';

import AppStatsService from './app-stats.service';
import { type IAppStats } from '@/shared/model/app-stats.model';
import { useAlertService } from '@/shared/alert/alert.service';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AppStats',
  setup() {
    const { t: t$ } = useI18n();
    const appStatsService = inject('appStatsService', () => new AppStatsService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const appStats: Ref<IAppStats[]> = ref([]);

    const isFetching = ref(false);

    const clear = () => {};

    const retrieveAppStatss = async () => {
      isFetching.value = true;
      try {
        const res = await appStatsService().retrieve();
        appStats.value = res.data;
      } catch (err) {
        alertService.showHttpError(err.response);
      } finally {
        isFetching.value = false;
      }
    };

    const handleSyncList = () => {
      retrieveAppStatss();
    };

    onMounted(async () => {
      await retrieveAppStatss();
    });

    const removeId: Ref<number> = ref(null);
    const removeEntity = ref<any>(null);
    const prepareRemove = (instance: IAppStats) => {
      removeId.value = instance.id;
      removeEntity.value.show();
    };
    const closeDialog = () => {
      removeEntity.value.hide();
    };
    const removeAppStats = async () => {
      try {
        await appStatsService().delete(removeId.value);
        const message = t$('kaizenMobileApp.appStats.deleted', { param: removeId.value }).toString();
        alertService.showInfo(message, { variant: 'danger' });
        removeId.value = null;
        retrieveAppStatss();
        closeDialog();
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    return {
      appStats,
      handleSyncList,
      isFetching,
      retrieveAppStatss,
      clear,
      removeId,
      removeEntity,
      prepareRemove,
      closeDialog,
      removeAppStats,
      t$,
    };
  },
});
