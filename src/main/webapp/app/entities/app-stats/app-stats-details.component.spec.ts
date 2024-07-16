/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import AppStatsDetails from './app-stats-details.vue';
import AppStatsService from './app-stats.service';
import AlertService from '@/shared/alert/alert.service';

type AppStatsDetailsComponentType = InstanceType<typeof AppStatsDetails>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const appStatsSample = { id: 123 };

describe('Component Tests', () => {
  let alertService: AlertService;

  afterEach(() => {
    vitest.resetAllMocks();
  });

  describe('AppStats Management Detail Component', () => {
    let appStatsServiceStub: SinonStubbedInstance<AppStatsService>;
    let mountOptions: MountingOptions<AppStatsDetailsComponentType>['global'];

    beforeEach(() => {
      route = {};
      appStatsServiceStub = sinon.createStubInstance<AppStatsService>(AppStatsService);

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'router-link': true,
        },
        provide: {
          alertService,
          appStatsService: () => appStatsServiceStub,
        },
      };
    });

    describe('Navigate to details', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        appStatsServiceStub.find.resolves(appStatsSample);
        route = {
          params: {
            appStatsId: '' + 123,
          },
        };
        const wrapper = shallowMount(AppStatsDetails, { global: mountOptions });
        const comp = wrapper.vm;
        // WHEN
        await comp.$nextTick();

        // THEN
        expect(comp.appStats).toMatchObject(appStatsSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        appStatsServiceStub.find.resolves(appStatsSample);
        const wrapper = shallowMount(AppStatsDetails, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
