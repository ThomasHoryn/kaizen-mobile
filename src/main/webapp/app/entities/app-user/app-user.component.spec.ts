/* tslint:disable max-line-length */
import { vitest } from 'vitest';
import { shallowMount, type MountingOptions } from '@vue/test-utils';
import sinon, { type SinonStubbedInstance } from 'sinon';

import AppUser from './app-user.vue';
import AppUserService from './app-user.service';
import AlertService from '@/shared/alert/alert.service';

type AppUserComponentType = InstanceType<typeof AppUser>;

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  let alertService: AlertService;

  describe('AppUser Management Component', () => {
    let appUserServiceStub: SinonStubbedInstance<AppUserService>;
    let mountOptions: MountingOptions<AppUserComponentType>['global'];

    beforeEach(() => {
      appUserServiceStub = sinon.createStubInstance<AppUserService>(AppUserService);
      appUserServiceStub.retrieve.resolves({ headers: {} });

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
          appUserService: () => appUserServiceStub,
        },
      };
    });

    describe('Mount', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        appUserServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

        // WHEN
        const wrapper = shallowMount(AppUser, { global: mountOptions });
        const comp = wrapper.vm;
        await comp.$nextTick();

        // THEN
        expect(appUserServiceStub.retrieve.calledOnce).toBeTruthy();
        expect(comp.appUsers[0]).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
    describe('Handles', () => {
      let comp: AppUserComponentType;

      beforeEach(async () => {
        const wrapper = shallowMount(AppUser, { global: mountOptions });
        comp = wrapper.vm;
        await comp.$nextTick();
        appUserServiceStub.retrieve.reset();
        appUserServiceStub.retrieve.resolves({ headers: {}, data: [] });
      });

      it('Should call delete service on confirmDelete', async () => {
        // GIVEN
        appUserServiceStub.delete.resolves({});

        // WHEN
        comp.prepareRemove({ id: 123 });

        comp.removeAppUser();
        await comp.$nextTick(); // clear components

        // THEN
        expect(appUserServiceStub.delete.called).toBeTruthy();

        // THEN
        await comp.$nextTick(); // handle component clear watch
        expect(appUserServiceStub.retrieve.callCount).toEqual(1);
      });
    });
  });
});
