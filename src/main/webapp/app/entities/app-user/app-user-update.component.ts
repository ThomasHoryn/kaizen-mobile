import { computed, defineComponent, inject, ref, type Ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { useRoute, useRouter } from 'vue-router';
import { useVuelidate } from '@vuelidate/core';

import AppUserService from './app-user.service';
import { useValidation } from '@/shared/composables';
import { useAlertService } from '@/shared/alert/alert.service';

import UserService from '@/entities/user/user.service';
import { type IAppUser, AppUser } from '@/shared/model/app-user.model';

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'AppUserUpdate',
  setup() {
    const appUserService = inject('appUserService', () => new AppUserService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const appUser: Ref<IAppUser> = ref(new AppUser());
    const userService = inject('userService', () => new UserService());
    const users: Ref<Array<any>> = ref([]);
    const isSaving = ref(false);
    const currentLanguage = inject('currentLanguage', () => computed(() => navigator.language ?? 'en'), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);

    const retrieveAppUser = async appUserId => {
      try {
        const res = await appUserService().find(appUserId);
        appUser.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.appUserId) {
      retrieveAppUser(route.params.appUserId);
    }

    const initRelationships = () => {
      userService()
        .retrieve()
        .then(res => {
          users.value = res.data;
        });
    };

    initRelationships();

    const { t: t$ } = useI18n();
    const validations = useValidation();
    const validationRules = {
      tenantId: {
        minLength: validations.minLength(t$('entity.validation.minlength', { min: 1 }).toString(), 1),
        maxLength: validations.maxLength(t$('entity.validation.maxlength', { max: 254 }).toString(), 254),
      },
      internalUser: {},
    };
    const v$ = useVuelidate(validationRules, appUser as any);
    v$.value.$validate();

    return {
      appUserService,
      alertService,
      appUser,
      previousState,
      isSaving,
      currentLanguage,
      users,
      v$,
      t$,
    };
  },
  created(): void {},
  methods: {
    save(): void {
      this.isSaving = true;
      if (this.appUser.id) {
        this.appUserService()
          .update(this.appUser)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showInfo(this.t$('kaizenMobileApp.appUser.updated', { param: param.id }));
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      } else {
        this.appUserService()
          .create(this.appUser)
          .then(param => {
            this.isSaving = false;
            this.previousState();
            this.alertService.showSuccess(this.t$('kaizenMobileApp.appUser.created', { param: param.id }).toString());
          })
          .catch(error => {
            this.isSaving = false;
            this.alertService.showHttpError(error.response);
          });
      }
    },
  },
});
