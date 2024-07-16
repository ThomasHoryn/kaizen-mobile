/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';
import { type RouteLocation } from 'vue-router';

import AppStatsUpdate from './app-stats-update.vue';
import AppStatsService from './app-stats.service';
import AlertService from '@/shared/alert/alert.service';

type AppStatsUpdateComponentType = InstanceType<typeof AppStatsUpdate>;

let route: Partial<RouteLocation>;
const routerGoMock = vitest.fn();

vitest.mock('vue-router', () => ({
  useRoute: () => route,
  useRouter: () => ({ go: routerGoMock }),
}));

const appStatsSample = { id: 123 };

describe('Component Tests', () => {
  let mountOptions: MountingOptions<AppStatsUpdateComponentType>['global'];
  let alertService: AlertService;

  describe('AppStats Management Update Component', () => {
    let comp: AppStatsUpdateComponentType;
    let appStatsServiceStub: SinonStubbedInstance<AppStatsService>;

    beforeEach(() => {
      route = {};
      appStatsServiceStub = sinon.createStubInstance<AppStatsService>(AppStatsService);
      appStatsServiceStub.retrieve.onFirstCall().resolves(Promise.resolve([]));

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          'font-awesome-icon': true,
          'b-input-group': true,
          'b-input-group-prepend': true,
          'b-form-datepicker': true,
          'b-form-input': true,
        },
        provide: {
          alertService,
          appStatsService: () => appStatsServiceStub,
        },
      };
    });

    afterEach(() => {
      vitest.resetAllMocks();
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const wrapper = shallowMount(AppStatsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.appStats = appStatsSample;
        appStatsServiceStub.update.resolves(appStatsSample);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(appStatsServiceStub.update.calledWith(appStatsSample)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        appStatsServiceStub.create.resolves(entity);
        const wrapper = shallowMount(AppStatsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        comp.appStats = entity;

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(appStatsServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        appStatsServiceStub.find.resolves(appStatsSample);
        appStatsServiceStub.retrieve.resolves([appStatsSample]);

        // WHEN
        route = {
          params: {
            appStatsId: '' + appStatsSample.id,
          },
        };
        const wrapper = shallowMount(AppStatsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(comp.appStats).toMatchObject(appStatsSample);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        appStatsServiceStub.find.resolves(appStatsSample);
        const wrapper = shallowMount(AppStatsUpdate, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();

        comp.previousState();
        await comp.$nextTick();

        expect(routerGoMock).toHaveBeenCalledWith(-1);
      });
    });
  });
});
