import { defineComponent, provide } from 'vue';

import AppUserService from './app-user/app-user.service';
import AppStatsService from './app-stats/app-stats.service';
import UserService from '@/entities/user/user.service';
// jhipster-needle-add-entity-service-to-entities-component-import - JHipster will import entities services here

export default defineComponent({
  compatConfig: { MODE: 3 },
  name: 'Entities',
  setup() {
    provide('userService', () => new UserService());
    provide('appUserService', () => new AppUserService());
    provide('appStatsService', () => new AppStatsService());
    // jhipster-needle-add-entity-service-to-entities-component - JHipster will import entities services here
  },
});
