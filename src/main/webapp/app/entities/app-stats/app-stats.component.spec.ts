/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AppStats from './app-stats.vue';
import AppStatsService from './app-stats.service';
import AlertService from '@/shared/alert/alert.service';

type AppStatsComponentType = InstanceType<typeof AppStats>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('AppStats Management Component', () => {
    let appStatsServiceStub: SinonStubbedInstance<AppStatsService>;
    let mountOptions: MountingOptions<AppStatsComponentType>['global'];

    beforeEach(() => {
      appStatsServiceStub = sinon.createStubInstance<AppStatsService>(AppStatsService);
      appStatsServiceStub.retrieve.resolves({ headers: {} });

      alertService = new AlertService({
        i18n: { t: vitest.fn() } as any,
        bvToast: {
          toast: vitest.fn(),
        } as any,
      });

      mountOptions = {
        stubs: {
          bModal: bModalStub as any,
          'font-awesome-icon': true,
          'b-badge': true,
          'b-button': true,
          'router-link': true,
        },
        directives: {
          'b-modal': {},
        },
        provide: {
          alertService,
          appStatsService: () => appStatsServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        appStatsServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(AppStats, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(appStatsServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.appStats[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: AppStatsComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(AppStats, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        appStatsServiceStub.retrieve.reset();
        appStatsServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        appStatsServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeAppStats();
        await comp.$nextTick(); // clear components

        // THEN
        expect(appStatsServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(appStatsServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
