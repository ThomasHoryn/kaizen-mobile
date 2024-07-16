import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore
const Entities = () => import('@/entities/entities.vue');

const AppUser = () => import('@/entities/app-user/app-user.vue');
const AppUserUpdate = () => import('@/entities/app-user/app-user-update.vue');
const AppUserDetails = () => import('@/entities/app-user/app-user-details.vue');

const AppStats = () => import('@/entities/app-stats/app-stats.vue');
const AppStatsUpdate = () => import('@/entities/app-stats/app-stats-update.vue');
const AppStatsDetails = () => import('@/entities/app-stats/app-stats-details.vue');

// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default {
  path: '/',
  component: Entities,
  children: [
    {
      path: 'app-user',
      name: 'AppUser',
      component: AppUser,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-user/new',
      name: 'AppUserCreate',
      component: AppUserUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-user/:appUserId/edit',
      name: 'AppUserEdit',
      component: AppUserUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-user/:appUserId/view',
      name: 'AppUserView',
      component: AppUserDetails,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-stats',
      name: 'AppStats',
      component: AppStats,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-stats/new',
      name: 'AppStatsCreate',
      component: AppStatsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-stats/:appStatsId/edit',
      name: 'AppStatsEdit',
      component: AppStatsUpdate,
      meta: { authorities: [Authority.USER] },
    },
    {
      path: 'app-stats/:appStatsId/view',
      name: 'AppStatsView',
      component: AppStatsDetails,
      meta: { authorities: [Authority.USER] },
    },
    // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
  ],
};
