<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="appStats">
        <h2 class="jh-entity-heading" data-cy="appStatsDetailsHeading">
          <span v-text="t$('kaizenMobileApp.appStats.detail.title')"></span> {{ appStats.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="t$('kaizenMobileApp.appStats.usedTenantId')"></span>
          </dt>
          <dd>
            <span>{{ appStats.usedTenantId }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.back')"></span>
        </button>
        <router-link v-if="appStats.id" :to="{ name: 'AppStatsEdit', params: { appStatsId: appStats.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="t$('entity.action.edit')"></span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./app-stats-details.component.ts"></script>
